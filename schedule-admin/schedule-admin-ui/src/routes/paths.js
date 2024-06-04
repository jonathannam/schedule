function path(root, sublink) {
  return `${root}${sublink}`;
}

const ROOTS_AUTH = '/auth';
const ROOTS_DASHBOARD = '/dashboard';

export const PATH_AUTH = {
  root: ROOTS_AUTH,
  login: path(ROOTS_AUTH, '/login'),
  register: path(ROOTS_AUTH, '/register'),
};

export const PATH_PAGE = {
  comingSoon: '/coming-soon',
  maintenance: '/maintenance',
  page404: '/404',
  page500: '/500',
};

export const PATH_DASHBOARD = {
  root: ROOTS_DASHBOARD,
  profile: path(ROOTS_DASHBOARD, '/profile'),

  schedule: {
    root: path(ROOTS_DASHBOARD, '/schedule'),

    newschedule: path(ROOTS_DASHBOARD, '/schedule/schedule/new'),
    schedules: path(ROOTS_DASHBOARD, '/schedule/schedules'),

    newSetting: path(ROOTS_DASHBOARD, '/schedule/setting/new'),

    newEmail: path(ROOTS_DASHBOARD, '/schedule/email/new'),
    emails: path(ROOTS_DASHBOARD, '/schedule/emails'),

    newCategory: path(ROOTS_DASHBOARD, '/schedule/category/new'),
    categories: path(ROOTS_DASHBOARD, '/schedule/categories'),

    caches: path(ROOTS_DASHBOARD, '/schedule/caches'),

    newRole: path(ROOTS_DASHBOARD, '/schedule/role/new'),
    roles: path(ROOTS_DASHBOARD, '/schedule/roles'),

    newPrivilege: path(ROOTS_DASHBOARD, '/schedule/privilege/new'),
    privileges: path(ROOTS_DASHBOARD, '/schedule/privileges'),

    newUser: path(ROOTS_DASHBOARD, '/schedule/user/new'),
    users: path(ROOTS_DASHBOARD, '/schedule/users'),

  },

  dashboard: {
    analytics: path(ROOTS_DASHBOARD, '/analytics'),
  },
};

// export const PATH_DOCS = 'https://onthibanglaixe.net';
// export const PATH_DOCS = 'https://docs-minimals.vercel.app/introduction';
