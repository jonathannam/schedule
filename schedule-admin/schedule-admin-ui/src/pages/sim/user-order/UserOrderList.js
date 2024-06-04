import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link as RouterLink } from 'react-router-dom';
// @mui
import {
  Button,
  Card,
  Checkbox,
  Container,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TablePagination,
  TableRow
} from '@mui/material';
import { useSnackbar } from 'notistack';
// redux
import { useDispatch, useSelector } from 'react-redux';
import { TableNoData } from '../../../components/table';
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
import DataGridListToolbar from '../../../components/datagrid/DataGridListToolbar';
import DataGridMoreMenu from '../../../components/datagrid/DataGridMoreMenu';
import { FormProvider } from '../../../components/hook-form';
import TableFilterSlidebar from '../../../components/table/TableFilterSlidebar';
import useLocales from '../../../hooks/useLocales';
import { getUserOrders, setUserOrderSearch } from '../../../redux/slices/sim/sim.user-order';
import { deleteUserOrderAPI, deleteUserOrdersAPI } from '../../../service/schedule/sim.user-order.service';

// ----------------------------------------------------------------------


export default function UserOrderList() {
  const { translate } = useLocales();
  const { themeStretch } = useSettings();
  const { enqueueSnackbar } = useSnackbar();

  const TABLE_HEAD = [
    { id: 'id', label: translate('sim.userOrder.id'), alignRight: false, checked: false, sort: true },
    { id: 'name', label: translate('sim.userOrder.name'), alignRight: false, checked: true, sort: true },
    { id: 'simNumber', label: translate('sim.userOrder.simNumber'), alignRight: false, checked: true, sort: true },
    { id: 'note', label: translate('sim.userOrder.note'), alignRight: false, checked: false, sort: true },
    { id: 'address', label: translate('sim.userOrder.address'), alignRight: false, checked: true, sort: true },
    { id: 'phone', label: translate('sim.userOrder.phone'), alignRight: false, checked: true, sort: true },
    { id: 'status', label: translate('sim.userOrder.status'), alignRight: false, checked: true, sort: true },
    { id: 'createdAt', label: translate('sim.userOrder.createdAt'), alignRight: false, checked: true, sort: true },
    { id: '', label: translate('label.actions'), alignRight: true, checked: true, sort: false },
  ];

  const { userOrders, totalElements, numberOfElements, search, error } = useSelector((state) => state.userOrder);
  const dispatch = useDispatch();
  const [selected, setSelected] = useState([]);
  const [open, setOpen] = useState(false);
  const [selectedId, setSelectedId] = useState();
  const [openFilter, setOpenFilter] = useState(false);

  useEffect(() => {
    const timeout = setTimeout(() => {
      dispatch(getUserOrders());
    }, 500);

    return () => clearTimeout(timeout);
  }, [search]); // eslint-disable-line react-hooks/exhaustive-deps

  // sap xep
  const handleRequestSort = (property) => {
    const isAsc = search.orders[0].property === property && search.orders[0].order === 'asc';
    const order = isAsc ? 'desc' : 'asc';

    dispatch(
      setUserOrderSearch({
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
      const selected = userOrders.map((n) => n.id);
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
      setUserOrderSearch({
        ...search,
        page: 0,
        size: parseInt(event.target.value, 10),
      })
    );
  };

  const handleChangePage = (page) => {
    dispatch(
      setUserOrderSearch({
        ...search,
        page,
      })
    );
  };

  const handleFilterByName = (value) => {
    dispatch(
      setUserOrderSearch({
        ...search,
        value,
      })
    );
  };

  const handleDeleteUserOrder = async (id) => {
    setOpen(true);
    setSelectedId(id);
  };

  const confirmDeleteUserOrder = async () => {
    let resp;
    if (selected.length > 0) resp = await deleteUserOrdersAPI(selected);
    else resp = await deleteUserOrderAPI(selectedId);

    handleDeleteResponse(resp);
  };

  const handleDeleteUserOrders = async () => {
    setOpen(true);
  };

  const handleDeleteResponse = (resp) => {
    setOpen(false);
    if (resp.code === '200') {
      enqueueSnackbar(translate('message.deleteSuccess'), { variant: 'success' });
      dispatch(getUserOrders());
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

  return (
    <Page title={translate('sim.userOrder.listUserOrder')}>
      <Container maxWidth={themeStretch ? false : 'lg'}>
        <HeaderBreadcrumbs
          heading={translate('sim.userOrder.listUserOrder')}
          links={[
            { name: translate('menu.dashboard'), href: PATH_DASHBOARD.root },
            {
              name: translate('menu.sim'),
              href: PATH_DASHBOARD.sim.root,
            },
            { name: translate('menu.userOrder') },
          ]}
          action={
            <Button
              variant="contained"
              component={RouterLink}
              to={PATH_DASHBOARD.sim.newUserOrder}
              startIcon={<Iconify icon={'eva:plus-fill'} />}
            >
              {translate('button.new')}
            </Button>
          }
        />

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
          <DataGridListToolbar
            numSelected={selected.length}
            filterName={search.value}
            onFilterName={handleFilterByName}
            onDelete={() => handleDeleteUserOrders()}
            showFilter={handleOpenFilter}
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
                  {userOrders.map((row) => {
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

                          if (head.id === '')
                            return (
                              <TableCell align="right" key={head.id}>
                                <DataGridMoreMenu
                                  pathEdit={`${PATH_DASHBOARD.sim.root}/user-order/${id}/edit`}
                                  pathView={`${PATH_DASHBOARD.sim.root}/user-order/${id}/view`}
                                  onDelete={() => handleDeleteUserOrder(id)}
                                />
                              </TableCell>
                            );

                          return <TableCell key={head.id}>{row[head.id]}</TableCell>;
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
            rowsPerPageOptions={[10, 25, 50]}
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
          onSubmit={confirmDeleteUserOrder}
        />
      </Container>
    </Page>
  );
}
