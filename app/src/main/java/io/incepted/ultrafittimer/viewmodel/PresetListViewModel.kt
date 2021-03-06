package io.incepted.ultrafittimer.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import android.view.View
import io.incepted.ultrafittimer.R
import io.incepted.ultrafittimer.activity.PresetListActivity
import io.incepted.ultrafittimer.activity.SummaryActivity
import io.incepted.ultrafittimer.activity.TimerActivity
import io.incepted.ultrafittimer.db.DbRepository
import io.incepted.ultrafittimer.db.model.Preset
import io.incepted.ultrafittimer.db.source.LocalDataSource
import io.incepted.ultrafittimer.util.SingleLiveEvent
import javax.inject.Inject

class PresetListViewModel @Inject constructor(appContext: Application, val repository: DbRepository)
    : AndroidViewModel(appContext), LocalDataSource.OnPresetsLoadedListener, LocalDataSource.OnTimersForPresetsLoadedListener,
        LocalDataSource.OnPresetUpdateListener, LocalDataSource.OnPresetDeletedListener {

    val snackbarResource = SingleLiveEvent<Int>()

    val presetActionEvent = SingleLiveEvent<Int>()

    val openEditScreen = SingleLiveEvent<Long>()

    val openSummaryActivity = SingleLiveEvent<Bundle>()

    val finishActivity = SingleLiveEvent<Void>()

    val openTimerActivity = SingleLiveEvent<Bundle>()

    var presets = ObservableArrayList<Preset>()

    var presetsExist = ObservableBoolean(false)


    fun start() {
        loadPresets()
    }


    public fun loadPresets() {
        repository.getPresets(this)
    }


    private fun populateList(l: List<Preset>) {
        presets.clear()
        presets.addAll(l)
    }


    fun showPresetActionDialog(presetPosition: Int) {
        if (presetPosition == -1) return
        presetActionEvent.value = presetPosition
    }

    fun bookmarkItem(presetPosition: Int) {
        val presetToUpdate = presets[presetPosition]
        presetToUpdate.bookmarked = !presetToUpdate.bookmarked
        presets[presetPosition] = presetToUpdate
        repository.updatePreset(presetToUpdate, this)
    }


    fun editItem(presetPosition: Int) {
        openEditScreen.value = presets[presetPosition].id
    }


    fun showPresetDetail(presetPosition: Int) {
        val presetId: Long = presets[presetPosition].id ?: return
        val bundle = Bundle()
        bundle.putLong(SummaryActivity.EXTRA_KEY_SUMMARY_ID, presetId)
        bundle.putBoolean(SummaryActivity.EXTRA_KEY_SUMMARY_IS_PRESET, true)
        openSummaryActivity.value = bundle
    }


    fun deleteItem(presetPosition: Int) {
        val presetToDelete = presets.removeAt(presetPosition)
        repository.deletePreset(presetToDelete.id ?: return, this)
    }


    fun playPreset(presetPosition: Int) {
        val bundle = Bundle()
        bundle.putBoolean(TimerActivity.EXTRA_KEY_FROM_PRESET, true)
        bundle.putLong(TimerActivity.EXTRA_KEY_ID, presets[presetPosition].id ?: return) // TODO Handle error
        openTimerActivity.value = bundle
    }


    fun playPreset(presetId: Long?) {
        val bundle = Bundle()
        bundle.putBoolean(TimerActivity.EXTRA_KEY_FROM_PRESET, true)
        bundle.putLong(TimerActivity.EXTRA_KEY_ID, presetId ?: return) // TODO Handle error
        openTimerActivity.value = bundle
    }


    fun finishActivity() {
        finishActivity.value = null
    }


    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PresetListActivity.RC_PRESET_EDITED && resultCode == Activity.RESULT_OK) {
            // Returning from MainActivity for the preset edit.
            // Reload the preset list if RESULT_OK is set.
            snackbarResource.value = R.string.preset_action_edited
            loadPresets()
        }
    }


    // ------------------------------ Callbacks ----------------------------

    override fun onPresetsLoaded(presets: List<Preset>) {
        presetsExist.set(presets.isNotEmpty())
        populateList(presets)
        repository.getTimerSettingsForPresets(presets, this)
    }


    override fun onPresetsNotAvailable() {
        snackbarResource.value = R.string.error_unexpected
    }


    override fun onPresetTimerLoadCompleted(presets: List<Preset>) {
        presetsExist.set(presets.isNotEmpty())
        populateList(presets)
    }


    override fun onPresetTimerNotAvailable() {
        snackbarResource.value = R.string.error_unexpected
    }


    override fun onPresetUpdated() {
        snackbarResource.value = R.string.preset_action_updated
        populateList(presets.toList())
    }


    override fun onPresetUpdateNotAvailable() {
        snackbarResource.value = R.string.error_unexpected
    }


    override fun onPresetDeleted() {
        snackbarResource.value = R.string.preset_action_deleted
        populateList(presets.toList())
    }


    override fun onPresetDeletionNotAvailable() {
        snackbarResource.value = R.string.error_unexpected
    }


}
