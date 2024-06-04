// @mui
import { Container } from '@mui/material';
// redux
// routes
import { PATH_DASHBOARD } from '../../../routes/paths';
// hooks
import useSettings from '../../../hooks/useSettings';
// components
import HeaderBreadcrumbs from '../../../components/HeaderBreadcrumbs';
import Page from '../../../components/Page';
import useLocales from '../../../hooks/useLocales';
import UserNewForm from '../../../sections/schedule/user/UserNewForm';

// ----------------------------------------------------------------------

export default function MediaUserCreate() {
  const { themeStretch } = useSettings();
  const { translate } = useLocales();

  return (
    <Page title={translate('media.user.newUser')}>
      <Container maxWidth={themeStretch ? false : 'lg'}>
        <HeaderBreadcrumbs
          heading={translate('schedule.schedule.newUser')}
          links={[
            { name: translate('menu.dashboard'), href: PATH_DASHBOARD.root },
            {
              name: translate('menu.schedule'),
              href: PATH_DASHBOARD.schedule.root,
            },
            {
              name: translate('menu.user'),
              href: PATH_DASHBOARD.schedule.users,
            },
            { name: translate('schedule.schedule.newUser') },
          ]}
        />
        <UserNewForm />
      </Container>
    </Page>
  );
}
