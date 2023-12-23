package com.example.changecolorsapp.views.changecolor


import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.example.changecolorsapp.R
import com.example.changecolorsapp.model.colors.ColorsRepository
import com.example.changecolorsapp.model.colors.NamedColor
import com.example.changecolorsapp.views.changecolor.ChangeColorFragment.Screen
import com.example.foundation.model.EmptyProgress
import com.example.foundation.model.LoadingResource
import com.example.foundation.model.PercentageProgress
import com.example.foundation.model.Progress
import com.example.foundation.model.Resource
import com.example.foundation.model.SuccessResource
import com.example.foundation.model.getPercentage
import com.example.foundation.model.isInProgress
import com.example.foundation.model.sideeffects.navigator.Navigator
import com.example.foundation.model.sideeffects.resources.Resources
import com.example.foundation.model.sideeffects.toasts.Toasts
import com.example.foundation.util.finiteShareIn
import com.example.foundation.views.BaseViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors = MutableStateFlow<Resource<List<NamedColor>>>(LoadingResource())
    private val _currentColorId =
        savedStateHandle.getStateFlowFromSavedState("currentColorId", screen.currentColorId)
    private val _instantSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)
    private val _sampleSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)

    // main destination (contains merged values from _availableColors & _currentColorId)

    val viewState: Flow<Resource<ViewState>> =
        combine(
            _availableColors,
            _currentColorId,
            _instantSaveInProgress,
            _sampleSaveInProgress,
            ::mergeSources
        )

    val screenTitle: LiveData<String> = viewState.map { result ->
        return@map if (result is SuccessResource) {
            val currentColor = result.data.colorsList.first { it.selected }
            resources.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
        } else {
            resources.getString(R.string.change_color_screen_title_simple)
        }
    }.asLiveData()

    init {
        load()
        // initializing MediatorLiveData
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_instantSaveInProgress.value.isInProgress()) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {
        try {

            _instantSaveInProgress.value = PercentageProgress.START
            _sampleSaveInProgress.value = PercentageProgress.START
            // this code is launched asynchronously in other thread
            val currentColorId =
                _currentColorId.value ?: throw IllegalStateException("Color ID should not be NULL")
            val currentColor = colorsRepository.getById(currentColorId)

            val flow = colorsRepository.setCurrentColor(currentColor)
                .finiteShareIn(this)

            val instantJob = async {
                flow.collect {
                    _instantSaveInProgress.value = PercentageProgress(it)
                }
            }

            val sampledJob = async {
                flow.collect {
                    _sampleSaveInProgress.value = PercentageProgress(it)
                }
            }

            instantJob.await()
            sampledJob.await()
            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e !is CancellationException) toasts.toast(resources.getString(R.string.error_happened))
        } finally {
            _instantSaveInProgress.value = EmptyProgress
            _sampleSaveInProgress.value = EmptyProgress
        }

    }
    // method onSaved is called in main thread when async code is finished


    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        load()
    }

    /**
     * [MediatorLiveData] can listen other LiveData instances (even more than 1)
     * and combine their values.
     * Here we listen the list of available colors ([_availableColors] live-data) + current color id
     * ([_currentColorId] live-data) + whether save is in progress or not, then we use all of
     * these values in order to create a [ViewState] instance, which is in its turn rendered by fragment.
     */
    private fun mergeSources(
        colors: Resource<List<NamedColor>>,
        currentColorId: Long,
        saveInProgress: Progress,
        sampleSaveInProgress: Progress
    ): Resource<ViewState> {


        // map Resource<List<NamedColor>> to Resource<ViewState>
        return colors.map { colorsList ->
            ViewState(
                // map List<NamedColor> to List<NamedColorListItem>
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !saveInProgress.isInProgress(),
                showCancelButton = !saveInProgress.isInProgress(),
                showSaveProgressBar = saveInProgress.isInProgress(),
                saveProgressPercentage = saveInProgress.getPercentage(),
                saveProgressPercentageMessage = resources.getString(
                    R.string.percentage_value,
                    sampleSaveInProgress.getPercentage()
                )

            )
        }
    }

    private fun load() = into(_availableColors) {
        return@into colorsRepository.getAvailableColors()
    }


    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean,
        val saveProgressPercentage: Int,
        val saveProgressPercentageMessage: String,

        )

}