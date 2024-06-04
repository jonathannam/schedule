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
import EmailNewForm from '../../../sections/schedule/email/EmailNewForm';

// ----------------------------------------------------------------------

export default function SimCreate() {
  const { themeStretch } = useSettings();
  const { translate } = useLocales();
  const { pathname } = useLocation();
  const { id } = useParams();
  const isEdit = pathname.includes('edit');
  const isView = pathname.includes('view');
  const isNew = !isEdit && !isView;
  const { error, emails } = useSelector((state) => state.email);

  const email = emails.find(c => c.id === parseInt(id, 10));

  return (
    <Page title={isNew ? translate('schedule.email.newEmail') : email?.email}>
      <Container maxWidth={themeStretch ? false : 'lg'}>
        <HeaderBreadcrumbs
          heading={isNew ? translate('schedule.email.newEmail') : email?.email}
          links={[
            { name: translate('menu.dashboard'), href: PATH_DASHBOARD.root },
            {
              name: translate('menu.email'),
              href: PATH_DASHBOARD.schedule.emails,
            },
            { name: isNew ? translate('schedule.email.newEmail') : email?.email || '' },
          ]}
        />
        {error && (isEdit || isView) ? (
          <Box sx={{ py: 3 }}>
            <ErrorOccur error={error} />
          </Box>
        ) :
          <EmailNewForm isEdit={isEdit} currentItem={email} isView={isView} />
        }
      </Container>
    </Page>
  );
}
