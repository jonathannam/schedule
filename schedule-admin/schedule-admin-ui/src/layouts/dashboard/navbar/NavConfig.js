// routes
import { PATH_DASHBOARD } from '../../../routes/paths';
// components
import SvgIconStyle from '../../../components/SvgIconStyle';

// ----------------------------------------------------------------------
const getIcon = (name) => (
  <SvgIconStyle src={`${process.env.PUBLIC_URL}/icons/${name}.svg`} sx={{ width: 1, height: 1 }} />
);
const ICONS = {
  cache: getIcon('ic_cache'),
  video: getIcon('ic_booking'),
  user: getIcon('ic_user'),
  analytics: getIcon('ic_dashboard'),
  shop: getIcon('ic_ecommerce'),
  tag: getIcon('ic_tag'),
  tutor: getIcon('ic_tutor'),
  category: getIcon('ic_category'),
  setting: getIcon('ic_cog'),
  exam: getIcon('ic_exam'),
};
const navConfig = [
  // GENERAL
  {
    subheader: 'menu.general',
    items: [
      {
        title: 'menu.analytics',
        path: PATH_DASHBOARD.dashboard.analytics,
        icon: ICONS.analytics,
        hasRoles: ['ROLE_ADMIN'],
      },
    ],
  },
  // MANAGEMENT
  // ----------------------------------------------------------------------
  {
    subheader: 'menu.sim',
    items: [
      {
        title: 'menu.sim',
        path: PATH_DASHBOARD.schedule.schedules,
        icon: ICONS.shop,
        hasRoles: ['ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_SHOP', 'ROLE_MEMBER'],
        children: [
          { title: 'menu.sims', path: PATH_DASHBOARD.schedule.schedules },
          // { title: 'menu.order', path: PATH_DASHBOARD.schedule.orders, hasRoles: ['ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_SHOP'] },
          // { title: 'menu.userorder', path: PATH_DASHBOARD.sim.userOrders, hasRoles: ['ROLE_ADMIN'] },
          // { title: 'menu.operator', path: PATH_DASHBOARD.sim.operators, hasRoles: ['ROLE_ADMIN'] },
          { title: 'menu.category', path: PATH_DASHBOARD.sim.categories, hasRoles: ['ROLE_ADMIN'] },
        ],
      },
  
      {
        title: 'menu.user',
        path: PATH_DASHBOARD.schedule.users,
        icon: ICONS.user,
        hasRoles: ['ROLE_ADMIN'],
        children: [
          { title: 'menu.user', path: PATH_DASHBOARD.schedule.users },
          { title: 'menu.privilege', path: PATH_DASHBOARD.schedule.privileges },
          { title: 'menu.role', path: PATH_DASHBOARD.schedule.roles },
        ],
      },
      {
        title: 'menu.setting',
        path: PATH_DASHBOARD.schedule.caches,
        icon: ICONS.cache,
        hasRoles: ['ROLE_ADMIN'],
        children: [
          { title: 'menu.cache', path: PATH_DASHBOARD.schedule.caches },
          { title: 'menu.email', path: PATH_DASHBOARD.schedule.emails }
        ],
      },
    ],
  },
];
export default navConfig;
