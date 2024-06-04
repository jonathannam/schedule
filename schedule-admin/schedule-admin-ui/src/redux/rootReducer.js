import { combineReducers } from 'redux';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import mediaCacheReducer from './slices/sim/schedule.cache';
import categoryReducer from './slices/sim/schedule.category';
import emailReducer from './slices/sim/schedule.email';
import mediaPrivilegeReducer from './slices/sim/schedule.privilege';
import mediaRoleReducer from './slices/sim/schedule.role';
import scheduleReducer from './slices/sim/schedule.schedule';
import settingReducer from './slices/sim/schedule.setting';
import mediaUserReducer from './slices/sim/schedule.user';
// ----------------------------------------------------------------------

const rootPersistConfig = {
  key: 'root',
  storage,
  keyPrefix: 'redux-',
  blacklist: ['error'],
};

const rootReducer = combineReducers({
  email: persistReducer({ ...rootPersistConfig, key: 'schedule-email' }, emailReducer),
  schedule: persistReducer({ ...rootPersistConfig, key: 'schedule-schedule' }, scheduleReducer),
  setting: persistReducer({ ...rootPersistConfig, key: 'schedule-setting' }, settingReducer),
  category: persistReducer({ ...rootPersistConfig, key: 'schedule-category' }, categoryReducer),
  mediaUser: persistReducer({ ...rootPersistConfig, key: 'schedule-user' }, mediaUserReducer),
  mediaRole: persistReducer({ ...rootPersistConfig, key: 'schedule-role' }, mediaRoleReducer),
  mediaPrivilege: persistReducer({ ...rootPersistConfig, key: 'schedule-privilege' }, mediaPrivilegeReducer),
  mediaCache: persistReducer({ ...rootPersistConfig, key: 'schedule-cache' }, mediaCacheReducer),
});
export { rootPersistConfig, rootReducer };

