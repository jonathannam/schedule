import { lazy, Suspense } from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import LoadingScreen from '../components/LoadingScreen';
import { PATH_AFTER_LOGIN } from '../config';
import RoleBasedGuard from '../guards/RoleBasedGuard';


// ----------------------------------------------------------------------
const Loadable = (Component) => (props) => {
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const { pathname } = useLocation();
  const isDashboard = pathname.includes('/dashboard');

  return (
    <Suspense
      fallback={
        <LoadingScreen
          sx={{
            ...(!isDashboard && {
              top: 0,
              left: 0,
              width: 1,
              zIndex: 9999,
              position: 'fixed',
            }),
          }}
        />
      }
    >
      <Component {...props} />
    </Suspense>
  );
};

// media
const EmailCreate = Loadable(lazy(() => import('../pages/sim/email/EmailCreate')));
const EmailList = Loadable(lazy(() => import('../pages/sim/email/EmailList')));
const OrderCreate = Loadable(lazy(() => import('../pages/sim/order/OrderCreate')));
const SimCreate = Loadable(lazy(() => import('../pages/sim/sim/ScheduleCreate')));
const SimList = Loadable(lazy(() => import('../pages/sim/sim/ScheduleList')));
const MediaRoleCreate = Loadable(lazy(() => import('../pages/sim/role/MediaRoleCreate')));
const MediaRoleList = Loadable(lazy(() => import('../pages/sim/role/MediaRoleList')));
const CategoryCreate = Loadable(lazy(() => import('../pages/sim/category/CategoryCreate')));
const CategoryList = Loadable(lazy(() => import('../pages/sim/category/CategoryList')));
const MediaPrivilegeCreate = Loadable(lazy(() => import('../pages/sim/privilege/MediaPrivilegeCreate')));
const MediaPrivilegeList = Loadable(lazy(() => import('../pages/sim/privilege/MediaPrivilegeList')));
const UserList = Loadable(lazy(() => import('../pages/sim/user/MediaUserList')));
const UserEdit = Loadable(lazy(() => import('../pages/sim/user/MediaUserEdit')));
const MediaUserCreate = Loadable(lazy(() => import('../pages/sim/user/MediaUserCreate')));
const MediaCacheList = Loadable(lazy(() => import('../pages/sim/cache/MediaCacheList')));
const MediaCacheKeyList = Loadable(lazy(() => import('../pages/sim/cache/MediaCacheKeyList')));

const scheduleRoute = {
  path: 'sims',
  element: (
    <RoleBasedGuard accessibleRoles={['ROLE_ADMIN', 'ROLE_SHOP', 'ROLE_STAFF', 'ROLE_MEMBER']}>
      <Outlet />
    </RoleBasedGuard>
  ),
  children: [
    { element: <Navigate to={PATH_AFTER_LOGIN} replace />, index: true },

    { path: 'schedule/new', element: <SimCreate /> },
    { path: 'schedules', element: <SimList /> },
    { path: 'schedule/:id/edit', element: <SimCreate /> },
    { path: 'schedule/:id/view', element: <SimCreate /> },


    { path: 'email/new', element: <EmailCreate /> },
    { path: 'emails', element: <EmailList /> },
    { path: 'email/:id/edit', element: <EmailCreate /> },
    { path: 'email/:id/view', element: <EmailCreate /> },

    // category
    { path: 'category/new', element: <CategoryCreate /> },
    { path: 'categories', element: <CategoryList /> },
    { path: 'category/:id/edit', element: <CategoryCreate /> },
    { path: 'category/:id/view', element: <CategoryCreate /> },

    // role
    { path: 'role/new', element: <MediaRoleCreate /> },
    { path: 'roles', element: <MediaRoleList /> },
    { path: 'role/:id/edit', element: <MediaRoleCreate /> },
    { path: 'role/:id/view', element: <MediaRoleCreate /> },

    // privilege
    { path: 'privilege/new', element: <MediaPrivilegeCreate /> },
    { path: 'privileges', element: <MediaPrivilegeList /> },
    { path: 'privilege/:id/edit', element: <MediaPrivilegeCreate /> },
    { path: 'privilege/:id/view', element: <MediaPrivilegeCreate /> },

    // user
    { path: 'users', element: <UserList /> },
    { path: 'user/new', element: <MediaUserCreate /> },
    { path: 'user/:id/view', element: <UserEdit /> },
    { path: 'user/:id/edit/role', element: <UserEdit /> },
    { path: 'user/:id/edit/email', element: <UserEdit /> },
    { path: 'user/:id/edit/phone', element: <UserEdit /> },
    { path: 'user/:id/edit/status', element: <UserEdit /> },
    { path: 'user/:id/edit/uid', element: <UserEdit /> },
    { path: 'user/:id/edit/info', element: <UserEdit /> },
    { path: 'user/:id/reset/password', element: <UserEdit /> },

    // cache
    { path: 'caches', element: <MediaCacheList /> },
    { path: 'cache/:name/keys', element: <MediaCacheKeyList /> },
  ],
};

export default scheduleRoute;
