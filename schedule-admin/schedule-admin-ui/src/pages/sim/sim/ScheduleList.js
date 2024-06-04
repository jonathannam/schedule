import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link as RouterLink } from 'react-router-dom';
// @mui
import {
  Button,
  Card,
  Checkbox,
  Container,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TablePagination,
  TableRow,
  Typography,
} from '@mui/material';
import { useSnackbar } from 'notistack';
// redux
import { TableNoData } from '../../../components/table';
import { useDispatch, useSelector } from '../../../redux/store';
// utils
// routes
import { PATH_DASHBOARD } from '../../../routes/paths';
// hooks
import Iconify from '../../../components/Iconify';
import useSettings from '../../../hooks/useSettings';
// components
import HeaderBreadcrumbs from '../../../components/HeaderBreadcrumbs';
import Page from '../../../components/Page';
import Scrollbar from '../../../components/Scrollbar';
// sections
import ConfirmDialog from '../../../components/ConfirmDialog';
// sections
import DataGridListHead from '../../../components/datagrid/DataGridListHead';
import DataGridMoreMenu from '../../../components/datagrid/DataGridMoreMenu';
import SimGridListToolbar from '../../../components/datagrid/SimGridListToolbar';
import { FormProvider } from '../../../components/hook-form';
import TableFilterSlidebar from '../../../components/table/TableFilterSlidebar';
import useAuth from '../../../hooks/useAuth';
import useLocales from '../../../hooks/useLocales';
import { getSims, setSimSearch } from '../../../redux/slices/sim/schedule.schedule';
import { deleteSimAPI, deleteSimsAPI, importExcelSimAPI, updateSimStatusAPI } from '../../../service/schedule/schedule.schedule.service';
import { fCurrency } from '../../../utils/formatNumber';
import { roles } from '../../../utils/roles';

// ----------------------------------------------------------------------

const useCurrentRole = () => {
  const { user } = useAuth();
  return user?.role?.name;
};

const STATUS_OPTIONS = { "ACTIVE": "ACTIVE", "HOLD": "HOLD", "SOLD": "SOLD" };

export default function ScheduleList() {
  const { translate } = useLocales();
  const { themeStretch } = useSettings();
  const dispatch = useDispatch();
  const { enqueueSnackbar } = useSnackbar();
  const currentRole = useCurrentRole();

  const TABLE_HEAD = [
    { id: 'id', label: translate('schedule.schedule.id'), alignRight: false, checked: false, sort: true },
    { id: 'phoneNo', label: translate('schedule.schedule.phoneNo'), alignRight: false, checked: true, sort: true },
    { id: 'price', label: translate('schedule.schedule.price'), alignRight: false, checked: true, sort: true },
    currentRole === roles.ROLE_ADMIN && { id: 'profit', label: translate('schedule.schedule.profit'), alignRight: false, checked: true, sort: true },
    currentRole === roles.ROLE_ADMIN && { id: 'interestProfit', label: translate('schedule.schedule.interestProfit'), alignRight: false, checked: true, sort: true },
    { id: 'quickSale', label: translate('schedule.schedule.quickSale'), alignRight: false, checked: false, sort: true },
    { id: 'operator', label: translate('schedule.schedule.operator'), alignRight: false, checked: true, sort: false },
    { id: 'categories', label: translate('schedule.schedule.categories'), alignRight: false, checked: true, sort: false },
    { id: 'status', label: translate('schedule.schedule.status'), alignRight: false, checked: true, sort: true },
    { id: 'createdBy', label: translate('schedule.schedule.createdBy'), alignRight: false, checked: false, sort: true },
    { id: 'createdAt', label: translate('schedule.schedule.createdAt'), alignRight: false, checked: false, sort: true },
    (currentRole === roles.ROLE_ADMIN || currentRole === roles.ROLE_SHOP || currentRole === roles.ROLE_STAFF) && { id: '', label: translate('label.actions'), alignRight: true, checked: true, sort: false },
  ];

  const { sims, totalElements, numberOfElements, search, error } = useSelector((state) => state.sim);
  const [selected, setSelected] = useState([]);
  const [open, setOpen] = useState(false);
  const [selectedId, setSelectedId] = useState();
  const [openFilter, setOpenFilter] = useState(false);

  useEffect(() => {
    const timeout = setTimeout(() => {
      dispatch(getSims());
    }, 500);

    return () => clearTimeout(timeout);
  }, [search]); // eslint-disable-line react-hooks/exhaustive-deps

  // sap xep
  const handleRequestSort = (property) => {
    const isAsc = search.orders[0].property === property && search.orders[0].order === 'asc';
    const order = isAsc ? 'desc' : 'asc';

    dispatch(
      setSimSearch({
        ...search,
        orders: [
          {
            order,
            property,
          },
        ],
      })
    );
  };

  const handleSelectAllClick = (checked) => {
    if (checked) {
      const selected = sims.map((n) => n.id);
      setSelected(selected);
      return;
    }
    setSelected([]);
  };

  const handleClick = (id) => {
    const selectedIndex = selected.indexOf(id);
    let newSelected = [];
    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, id);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(selected.slice(0, selectedIndex), selected.slice(selectedIndex + 1));
    }
    setSelected(newSelected);
  };

  const handleChangeRowsPerPage = (event) => {
    dispatch(
      setSimSearch({
        ...search,
        page: 0,
        size: parseInt(event.target.value, 10),
      })
    );
  };

  const handleChangePage = (page) => {
    dispatch(
      setSimSearch({
        ...search,
        page,
      })
    );
  };

  const handleFilterByName = (value) => {
    dispatch(
      setSimSearch({
        ...search,
        value,
      })
    );
  };

  const handleFilterStartPrice = (startPrice) => {
    dispatch(
      setSimSearch({
        ...search,
        filterBys: {
          ...search.filterBys,
          startPrice
        }
      })
    );
  };

  const handleFilterEndPrice = (endPrice) => {
    dispatch(
      setSimSearch({
        ...search,
        filterBys: {
          ...search.filterBys,
          endPrice
        }
      })
    );
  };

  const handleDeleteSim = async (id) => {
    setOpen(true);
    setSelectedId(id);
  };

  const confirmDeleteSim = async () => {
    let resp;
    if (selected.length > 0) resp = await deleteSimsAPI(selected);
    else resp = await deleteSimAPI(selectedId);

    handleDeleteResponse(resp);
  };

  const handleDeleteSims = async () => {
    setOpen(true);
  };

  const handleDeleteResponse = (resp) => {
    setOpen(false);
    if (resp.code === '200') {
      enqueueSnackbar(translate('message.deleteSuccess'), { variant: 'success' });
      dispatch(getSims());
      setSelected([]);
    } else enqueueSnackbar(`${resp.code} - ${resp.message}`, { variant: 'error' });
  };

  const defaultValues = {
    checkedColumns: TABLE_HEAD.filter((item) => item.checked).map((item) => item.label),
  };

  const methods = useForm({
    defaultValues,
  });

  const { reset, watch } = methods;

  const { checkedColumns } = watch();

  const handleOpenFilter = () => {
    setOpenFilter(true);
  };

  const handleCloseFilter = () => {
    setOpenFilter(false);
  };

  const handleResetFilter = () => {
    reset();
    handleCloseFilter();
  };

  const [file, setFile] = useState('');

  const handleFileChange = (e) => {
    if (e.target.files) {
      setFile(e.target.files[0]);
    }
  };

  const handleImport = async () => {
    const resp = await importExcelSimAPI(file);
    if (resp.code === '200') {
      enqueueSnackbar(translate('message.createSuccess'), { variant: 'success' });
      dispatch(getSims());
      setSelected([]);
    } else enqueueSnackbar(`${resp.code} - ${resp.message}`, { variant: 'error' });
  };

  const handleHoldSim = async (value) => {
    let resp = null;
    if (value.status === STATUS_OPTIONS.ACTIVE) {
      const data = { ...value, status: STATUS_OPTIONS.HOLD };
      resp = await updateSimStatusAPI(data);
    } else if (value.status === STATUS_OPTIONS.HOLD) {
      const data = { ...value, status: STATUS_OPTIONS.ACTIVE };
      resp = await updateSimStatusAPI(data);
    }
    if (resp?.code === '200') {
      enqueueSnackbar(translate('message.updateSuccess'), { variant: 'success' });
      dispatch(getSims());
    } else enqueueSnackbar(`${resp.code} - ${resp.message}`, { variant: 'error' });
  };

  const handleCheckColor = (value) => {
    if (value === STATUS_OPTIONS.HOLD) {
      return '#0099CC';
    }
    if (value === STATUS_OPTIONS.ACTIVE) {
      return '#00ab55';
    }
    if (value === STATUS_OPTIONS.SOLD) {
      return '#EE0000';
    }
    return '#00b8d9';
  };

  return (
    <Page title={translate('schedule.schedule.listSchedule')}>
      <Container maxWidth={themeStretch ? false : 'lg'}>
        <HeaderBreadcrumbs
          heading={translate('schedule.schedule.listSchedule')}
          links={[
            { name: translate('menu.dashboard'), href: PATH_DASHBOARD.root },
            {
              name: translate('menu.sim'),
              href: PATH_DASHBOARD.schedule.root,
            },
          ]}
          action={
            (currentRole === roles.ROLE_ADMIN || currentRole === roles.ROLE_SHOP) &&
            <Stack spacing={1} direction={"row"}>
              <label htmlFor="upload-photo">
                <input
                  style={{ display: 'none' }}
                  id="upload-photo"
                  name="upload-photo"
                  type="file"
                  onChange={handleFileChange}
                />
                <div>{file && `${file.name}`}</div>
                <Button color="secondary" variant="contained" component="span">
                  Chọn file excel
                </Button>
              </label>
              <Button color="secondary" variant="contained" component="span" onClick={handleImport}>
                Tải lên
              </Button>
              <Button
                variant="contained"
                component={RouterLink}
                to={PATH_DASHBOARD.schedule.newschedule}
                startIcon={<Iconify icon={'eva:plus-fill'} />}
              >
                {translate('button.new')}
              </Button>
            </Stack>
          }
        />
        <input style={{ display: 'none' }} id="upload-photo" name="upload-file" type="file" />
        <FormProvider methods={methods}>
          <TableFilterSlidebar
            onResetAll={handleResetFilter}
            isOpen={openFilter}
            onOpen={handleOpenFilter}
            onClose={handleCloseFilter}
            columns={TABLE_HEAD.map((item) => item.label)}
          />
        </FormProvider>

        <Card>
          <SimGridListToolbar
            numSelected={selected.length}
            filterName={search.value}
            onFilterName={handleFilterByName}
            onDelete={() => handleDeleteSims()}
            showFilter={handleOpenFilter}
            onFilterStartPrice={handleFilterStartPrice}
            onFilterEndPrice={handleFilterEndPrice}
          />

          <Scrollbar>
            <TableContainer sx={{ minWidth: 800 }}>
              <Table>
                <DataGridListHead
                  order={search.orders[0].order}
                  orderBy={search.orders[0].property}
                  headLabel={TABLE_HEAD.filter((head) => checkedColumns.indexOf(head.label) > -1)}
                  rowCount={numberOfElements}
                  numSelected={selected.length}
                  onRequestSort={handleRequestSort}
                  onSelectAllClick={handleSelectAllClick}
                />

                <TableBody>
                  {sims.map((row) => {
                    const { id } = row;

                    const isItemSelected = selected.indexOf(id) !== -1;

                    return (
                      <TableRow
                        hover
                        key={id}
                        tabIndex={-1}
                        role="checkbox"
                        selected={isItemSelected}
                        aria-checked={isItemSelected}
                      >
                        <TableCell padding="checkbox">
                          <Checkbox checked={isItemSelected} onClick={() => handleClick(id)} />
                        </TableCell>
                        {TABLE_HEAD.map((head) => {
                          if (checkedColumns.indexOf(head.label) === -1) return null;

                          if (head.id === 'operator')
                            return (
                              <TableCell key={head.id}>
                                <Typography variant="subtitle2" noWrap>
                                  {row[head.id]?.title}
                                </Typography>
                              </TableCell>
                            );

                          if (head.id === 'createdBy')
                            return (
                              <TableCell key={head.id}>
                                <Typography variant="subtitle2" noWrap>
                                  {row[head.id]?.name}
                                </Typography>
                              </TableCell>
                            );

                          if (head.id === 'status')
                            return (
                              <TableCell key={head.id}>
                                <Typography
                                  variant="body2"
                                  sx={{
                                    textAlign: 'center',
                                    color: '#fff',
                                    borderRadius: 1.5,
                                    backgroundColor: handleCheckColor(row[head.id]),
                                  }}
                                  noWrap
                                >
                                  {row[head.id]}
                                </Typography>
                              </TableCell>
                            );

                          if (head.id === 'categories')
                            return (
                              <TableCell key={head.id}>
                                {row[head.id].map((categories, index) => (
                                  <Typography key={index}>{categories?.title}</Typography>
                                ))}
                              </TableCell>
                            );

                          if (head.id === '')
                            return (
                              <TableCell align="right" key={head.id}>
                                {row.status !== STATUS_OPTIONS.SOLD &&
                                  <Button color="secondary" variant="contained" component="span" onClick={() => handleHoldSim(row)} >
                                    {(row.status === STATUS_OPTIONS.ACTIVE) ? "Giữ sim" : "Bỏ giữ sim"}
                                  </Button>
                                }
                                {row.status !== STATUS_OPTIONS.SOLD && currentRole === roles.ROLE_STAFF &&
                                  <Button
                                    variant="contained"
                                    component={RouterLink}
                                    to={`${PATH_DASHBOARD.sim.root}/order/new/${id}`}
                                    startIcon={<Iconify icon={'raphael:cart'} />}
                                  >
                                    {translate('button.order')}
                                  </Button>
                                }
                                {
                                  (currentRole === roles.ROLE_ADMIN || currentRole === roles.ROLE_SHOP) &&
                                  <DataGridMoreMenu
                                    pathEdit={`${PATH_DASHBOARD.sim.root}/sim/${id}/edit`}
                                    pathView={`${PATH_DASHBOARD.sim.root}/sim/${id}/view`}
                                    onDelete={() => handleDeleteSim(id)}
                                  />
                                }
                              </TableCell>
                            );

                          if (head.id === 'price' || head.id === 'interestProfit' || head.id === 'profit')
                            return (
                              <TableCell key={head.id}>
                                <Typography variant="subtitle2" noWrap>
                                  {fCurrency(row[head.id])}
                                </Typography>
                              </TableCell>
                            );

                          return <TableCell key={head.id}>{row[head.id].toString()}</TableCell>;
                        })}
                      </TableRow>
                    );
                  })}

                  <TableNoData
                    isNotFound={numberOfElements === 0}
                    error={error}
                    length={checkedColumns.length + 1}
                    searchQuery={search.value}
                  />
                </TableBody>
              </Table>
            </TableContainer>
          </Scrollbar>

          <TablePagination
            component="div"
            count={totalElements}
            rowsPerPage={search.size}
            page={search.page}
            onPageChange={(event, value) => handleChangePage(value)}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </Card>
        <ConfirmDialog
          values={{ name: translate('message.dialogDeleteTitle'), content: translate('message.dialogDeleteContent') }}
          onClose={() => setOpen(false)}
          isOpen={open}
          onSubmit={confirmDeleteSim}
        />
      </Container>
    </Page >
  );
}
