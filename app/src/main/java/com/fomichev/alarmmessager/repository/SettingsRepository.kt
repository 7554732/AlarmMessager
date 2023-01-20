package com.fomichev.alarmmessager.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.fomichev.alarmmessager.domain.AlarmCFG
import com.fomichev.alarmmessager.domain.Msg
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext val context: Context){

    val IS_STARTED = booleanPreferencesKey("is_started")
    val TIME_TO_ALARM = intPreferencesKey("time_to_alarm")
    val TIME_TO_MSG = intPreferencesKey("time_to_msg")

    val alarmCfgFlow: Flow<AlarmCFG> = context.dataStore.data
        .map { preferences ->
            AlarmCFG(
                preferences[TIME_TO_ALARM] ?: 30,
                preferences[TIME_TO_MSG] ?: 10,
                preferences[IS_STARTED] ?: false
            )
        }

    val PHONE_NUMBER = stringPreferencesKey("phone_number")
    val MSG_TEXT = stringPreferencesKey("msg_text")

    val msgFlow: Flow<Msg> = context.dataStore.data
        .map { preferences ->
            Msg(
                preferences[PHONE_NUMBER] ?: "",
                preferences[MSG_TEXT] ?: ""
            )
        }

    suspend fun saveMsg(msg: Msg) {
        context.dataStore.edit { preferences ->
            preferences[PHONE_NUMBER] = msg.phoneNumber
            preferences[MSG_TEXT] = msg.text
        }
    }

    suspend fun saveAlarmCFG(cfg: AlarmCFG) {
        context.dataStore.edit { preferences ->
            preferences[TIME_TO_ALARM] = cfg.timeToAlarm
            preferences[TIME_TO_MSG] = cfg.timeToMsg
            preferences[IS_STARTED] = cfg.isStarted
        }
    }
}