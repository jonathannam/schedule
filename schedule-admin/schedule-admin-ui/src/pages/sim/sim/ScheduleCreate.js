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
import SimNewForm from '../../../sections/schedule/sim/SimNewForm';

// ----------------------------------------------------------------------

export default function ScheduleCreate() {
  const { themeStretch } = useSettings();
  const { translate } = useLocales();
  const { pathname } = useLocation();
  const { id } = useParams();
  const isEdit = pathname.includes('edit');
  const isView = pathname.includes('view');
  const isNew = !isEdit && !isView;
  const { error, sims } = useSelector((state) => state.sim);

  const sim = sims.find(c => c.id === parseInt(id, 10));

  return (
    <Page title={isNew ? translate('sim.sim.newSim') : sim?.phoneNo}>
      <Container maxWidth={themeStretch ? false : 'lg'}>
        <HeaderBreadcrumbs
          heading={isNew ? translate('sim.sim.newSim') : sim?.phoneNo}
          links={[
            { name: translate('menu.dashboard'), href: PATH_DASHBOARD.root },
            {
              name: translate('menu.sim'),
              href: PATH_DASHBOARD.schedule.root,
            },
            {
              name: translate('menu.sims'),
              href: PATH_DASHBOARD.schedule.sims,
            },
            { name: isNew ? translate('sim.sim.newSim') : sim?.phoneNo || '' },
          ]}
        />
        {error && (isEdit || isView) ? (
          <Box sx={{ py: 3 }}>
            <ErrorOccur error={error} />
          </Box>
        ) :
          <SimNewForm isEdit={isEdit} currentItem={sim} isView={isView} />
        }
      </Container>
    </Page>
  );
}
