import { useLocation, useParams } from 'react-router-dom';
// @mui
import { Box, Container } from '@mui/material';
// redux
import { useSelector } from '../../../redux/store';
// routes
import { PATH_DASHBOARD } from '../../../routes/paths';
// hooks
import useSettings from '../../../hooks/useSettings';
// components
import ErrorOccur from '../../../components/ErrorOccur';
import HeaderBreadcrumbs from '../../../components/HeaderBreadcrumbs';
import Page from '../../../components/Page';
import useLocales from '../../../hooks/useLocales';
import UserOrderNewForm from '../../../sections/schedule/user-order/UserOrderNewForm';

// ----------------------------------------------------------------------

export default function UserOrderCreate() {
  const { themeStretch } = useSettings();
  const { translate } = useLocales();
  const { pathname } = useLocation();
  const { id } = useParams();
  const isEdit = pathname.includes('edit');
  const isView = pathname.includes('view');
  const isNew = !isEdit && !isView;
  const { userOrders, error } = useSelector((state) => state.userOrder);

  const userOrder = userOrders.find((c) => c.id === parseInt(id, 10));

  return (
    <Page title={isNew ? translate('sim.userOrder.newUserOrder') : userOrder?.phone}>
      <Container maxWidth={themeStretch ? false : 'lg'}>
        <HeaderBreadcrumbs
          heading={isNew ? translate('sim.userOrder.newUserOrder') : userOrder?.phone}
          links={[
            { name: translate('menu.dashboard'), href: PATH_DASHBOARD.root },
            {
              name: translate('menu.sim'),
              href: PATH_DASHBOARD.sim.root,
            },
            {
              name: translate('menu.userOrder'),
              href: PATH_DASHBOARD.sim.userOrders,
            },
            { name: isNew ? translate('sim.userOrder.newUserOrder') : userOrder?.phone || '' },
          ]}
        />
        {error && (isEdit || isView) ? (
          <Box sx={{ py: 3 }}>
            <ErrorOccur error={error} />
          </Box>
        ) : (
          <UserOrderNewForm isEdit={isEdit} currentItem={isNew ? null : userOrder} isView={isView} />
        )}
      </Container>
    </Page>
  );
}
