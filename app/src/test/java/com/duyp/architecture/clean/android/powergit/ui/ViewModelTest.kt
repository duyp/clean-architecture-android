import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.annotation.CallSuper
import com.duyp.architecture.clean.android.powergit.rules.TrampolineSchedulerRule
import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import com.duyp.architecture.clean.android.powergit.utils.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Base class for all ViewModel unit tests and integration tests. The ViewModel will be created before any test
 * cases, and the view state will be observed immediately after that by an state observer which provide assertions to
 * verify state value.
 * @param [State] type of view state
 * @param [Intent] type of intent
 * @param [VM] type of ViewModel, must extend [BaseViewModel]
*/
@RunWith(MockitoJUnitRunner::class)
abstract class ViewModelTest<State, Intent, VM : BaseViewModel<State, Intent>> {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulerRule = TrampolineSchedulerRule()

    private val mIntents = PublishSubject.create<Intent>()

    private lateinit var mStateObserver: TestObserver<State>

    protected lateinit var mViewModel: VM

    @Before
    @CallSuper
    open fun setup() {
        mViewModel = createViewModel()
        mStateObserver = TestObserver()
        mViewModel.state.observeForever(mStateObserver)
    }

    @After
    @CallSuper
    open fun tearDown() {
        mViewModel.state.removeObserver(mStateObserver)
    }

    protected abstract fun createViewModel(): VM

    protected fun viewState(): TestObserver<State> = mStateObserver

    /**
     * Post an intent
     */
    protected fun intent(intent: Intent) {
        mIntents.onNext(intent)
    }

    /**
     * Start processing intent (like activity / fragment onCreate)
     */
    protected fun processIntents() {
        mViewModel.processIntents(mIntents)
    }
}
