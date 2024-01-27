package com.nagi.ddtools.ui.toolpage.tools.groupwho

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nagi.ddtools.database.AppDatabase
import com.nagi.ddtools.database.activityList.ActivityList
import com.nagi.ddtools.database.activityList.ActivityListDao
import com.nagi.ddtools.database.idolGroupList.IdolGroupList
import com.nagi.ddtools.database.idolGroupList.IdolGroupListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ChooseWhoViewModel : ViewModel() {
    private val _data = MutableLiveData<ActivityList>()
    val data: LiveData<ActivityList> = _data

    private val _groupList = MutableLiveData<List<IdolGroupList>>()
    val groupList: LiveData<List<IdolGroupList>> = _groupList

    private val activityListDao: ActivityListDao by lazy {
        AppDatabase.getInstance().activityListDao()
    }
    private val groupListDao: IdolGroupListDao by lazy {
        AppDatabase.getInstance().idolGroupListDao()
    }

    fun initData(name: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val activityList = activityListDao.getByName(name)
                _data.postValue(activityList)
                val participatingGroupJson = activityList.participating_group
                val groupItemsJsonArray = JSONArray(participatingGroupJson)
                val resultList = mutableListOf<IdolGroupList>()
                for (i in 0 until groupItemsJsonArray.length()) {
                    val groupItemJson = groupItemsJsonArray.getJSONObject(i)
                    val groupName = groupItemJson.optString("name")
                    if (groupName.isNotBlank()) {
                        groupListDao.getById(groupName.toInt()).let { group ->
                            resultList.add(group)
                        }
                    }
                }
                _groupList.postValue(resultList)
            }
        }
    }

    fun removeData(position: Int) {
        val currentList = _groupList.value!!.toMutableList()
        if (position >= 0 && position < currentList.size) {
            currentList.removeAt(position)
            _groupList.value = currentList
        }
    }

}