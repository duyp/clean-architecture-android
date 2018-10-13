package com.duyp.architecture.clean.android.powergit
import android.animation.ValueAnimator
import android.app.Activity
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.arch.lifecycle.*
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.duyp.architecture.clean.android.powergit.ui.Event
import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import io.reactivex.Observable
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import timber.log.Timber

/**
 * Implementation of lazy that is not thread safe. Useful when you know what thread you will be
 * executing on and are not worried about synchronization.
 */
fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}

/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

// region ViewModels

/**
 * For Actvities, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(this, provider).get(VM::class.java)

/**
 * For Fragments, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(this, provider).get(VM::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the Activity.
 */
inline fun <reified VM : ViewModel> Fragment.activityViewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(requireActivity(), provider).get(VM::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the parent
 * Fragment.
 */
inline fun <reified VM : ViewModel> Fragment.parentViewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProviders.of(parentFragment!!, provider).get(VM::class.java)

// endregion
// region Parcelables, Bundles

/** Write an enum value to a Parcel */
fun <T : Enum<T>> Parcel.writeEnum(value: T) = writeString(value.name)

/** Read an enum value from a Parcel */
inline fun <reified T : Enum<T>> Parcel.readEnum(): T = enumValueOf(readString())

/** Write an enum value to a Bundle */
fun <T : Enum<T>> Bundle.putEnum(key: String, value: T) = putString(key, value.name)

/** Read an enum value from a Bundle */
inline fun <reified T : Enum<T>> Bundle.getEnum(key: String): T = enumValueOf(getString(key))

/** Write a boolean to a Parcel (copied from Parcel, where this is @hidden). */
fun Parcel.writeBoolean(value: Boolean) = writeInt(if (value) 1 else 0)

/** Read a boolean from a Parcel (copied from Parcel, where this is @hidden). */
fun Parcel.readBoolean() = readInt() != 0

// endregion

// =====================================================================================================================
// region LiveData
// =====================================================================================================================
/** Uses `Transformations.map` on a LiveData */
fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}

/** Uses `Transformations.switchMap` on a LiveData */
fun <X, Y> LiveData<X>.switchMap(body: (X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this, body)
}

fun <T> MutableLiveData<T>.setValueIfNew(newValue: T) {
    if (this.value != newValue) value = newValue
}

fun <T> MutableLiveData<T>.postValueIfNew(newValue: T) {
    if (this.value != newValue) postValue(newValue)
}

/**
 * Observes only non-null content
 */
fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, nonNullContent: T.() -> Unit) {
    this.observe(owner, Observer {
        it?.let(nonNullContent)
    })
}
// endregion
// =====================================================================================================================

// region ZonedDateTime
//fun ZonedDateTime.toEpochMilli() = this.toInstant().toEpochMilli()
// endregion

/**
 * Helper to force a when statement to assert all options are matched in a when statement.
 *
 * By default, Kotlin doesn't care if all branches are handled in a when statement. However, if you
 * use the when statement as an expression (with a value) it will force all cases to be handled.
 *
 * This helper is to make a lightweight way to say you meant to match all of them.
 *
 * Usage:
 *
 * ```
 * when(sealedObject) {
 *     is OneType -> //
 *     is AnotherType -> //
 * }.checkAllMatched
 */
val <T> T.checkAllMatched: T
    get() = this

// region UI utils

/**
 * Retrieves a color from the theme by attributes. If the attribute is not defined, a fall back
 * color will be returned.
 */
@ColorInt
fun Context.getThemeColor(
        @AttrRes attrResId: Int,
        @ColorRes fallbackColorResId: Int
): Int {
    val tv = TypedValue()
    return if (theme.resolveAttribute(attrResId, tv, true)) {
        tv.data
    } else {
        ContextCompat.getColor(this, fallbackColorResId)
    }
}

// endregion

/**
 * Helper to throw exceptions only in Debug builds, logging a warning otherwise.
 */
fun exceptionInDebug(t: Throwable) {
    if (BuildConfig.DEBUG) {
        throw t
    } else {
        Timber.e(t)
    }
}

// =====================================================================================================================
// Activity, fragment, view...
//

/**
 * Allows calls like
 *
 * `viewGroup.inflate(R.layout.foo)`
 */
fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

/**
 * Allows calls like
 *
 * `supportFragmentManager.inTransaction { add(...) }`
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

/**
 * Create a [Fragment] instance with bundle consumer
 */
fun <F: Fragment> fragmentInstance(fragment: () -> F, bundle: Bundle.() -> Unit): F {
    val f = fragment()
    val b = Bundle()
    bundle(b)
    f.arguments = b
    return f
}

inline fun <reified T: Fragment> Fragment.withArguments(bundle: Bundle.() -> Unit): T {
    val b = Bundle()
    bundle(b)
    this.arguments = b
    return this as T
}

fun Activity.showToastMessage(message: String) {
    if (!message.isEmpty()) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.showToastMessage(message: String) {
    if (this.context != null && !message.isEmpty()) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showKeyboard(editText: EditText) {
    activity?.showKeyboard(editText)
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(yourEditText: EditText) {
    try {
        val input = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        input.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        input.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT)
    } catch (ignored: Exception) { }
}

fun ViewPager.addOnPageSelectedListener(page: (Int) -> Unit) {
    this.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            page(position)
        }
    })
}

fun BottomNavigation.setOnItemClickListener(listener: (id: Int, position: Int, fromUser: Boolean) -> Unit) {
    this.setOnMenuItemClickListener(object: BottomNavigation.OnMenuItemSelectionListener {

        override fun onMenuItemSelect(id: Int, position: Int, fromUser: Boolean) {
            listener(id, position, fromUser)
        }

        override fun onMenuItemReselect(id: Int, position: Int, fromUser: Boolean) {}
    })
}

fun EditText.addSimpleTextChangedListener(listener: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

    })
}

fun RecyclerView.addSimpleOnScrollListener(listener: () -> Unit) {
    this.addOnScrollListener(object: RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            listener()
        }
    })
}

fun clearText(vararg textViews: TextView) {
    textViews.iterator().forEach { it.text = "" }
}

fun TextView.startCustomAnimation(isCollapsing: Boolean, finalText: String, duration: Long) {
    cancelAnimation()
    val mStartText = this.text
    val animator =
            if (isCollapsing) ValueAnimator.ofFloat(1.0f, 0.0f)
            else ValueAnimator.ofFloat(0.0f, 1.0f)
    animator.addUpdateListener {
        val currentValue = it.animatedValue as Float
        val ended = (isCollapsing && currentValue == 0.0f) || (!isCollapsing && currentValue == 1.0f)
        if (ended) {
            this.text = finalText
        } else {
            val n = (mStartText.length * currentValue).toInt()
            val text = mStartText.substring(0, n)
            if (text != this.text) {
                this.text = text
            }
        }
    }
    this.tag = animator
    animator.duration = duration
    animator.start()
}

fun TextView.startExpandingAnimation(text: String, duration: Long) {
    cancelAnimation()
    val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
    animator.addUpdateListener {
        val currentValue = it.animatedValue as Float
        val ended = currentValue == 1.0f
        if (ended) {
            this.text = text
        } else {
            val n = (text.length * currentValue).toInt()
            val currentText = text.substring(0, n)
            if (currentText != this.text) {
                this.text = currentText
            }
        }
    }
    this.tag = animator
    animator.duration = duration
    animator.start()
}

fun TextView.startCollapsingAnimation(finalText: String, duration: Long) {
    this.startCustomAnimation(true, finalText, duration)
}

fun TextView.cancelAnimation() {
    val o = this.tag
    if (o != null && o is ValueAnimator) {
        o.cancel()
    }
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

//
// End Activity, fragment, view...
// =====================================================================================================================

/**
 * Observes state of a [BaseViewModel] in a [LifecycleOwner] only if the emitted state is not null
 */
fun <S, I, VM : BaseViewModel<S, I>> LifecycleOwner.withState(vm: VM, state: S.() -> Unit) {
    vm.state.observeNonNull(this, state)
}

/**
 * Get an [Event] value only if the event is NOT NULL and its content hasn't been handled yet
 */
fun <T> event(event: Event<T>?, onEventUnhandledContent: T.() -> Unit) {
    event?.get(onEventUnhandledContent)
}

fun Throwable.printStacktraceIfDebug() {
    if (BuildConfig.DEBUG) this.printStackTrace()
}

fun <T> Observable<T>.onErrorResumeEmpty(): Observable<T> {
    return this.onErrorResumeNext { _: Throwable -> Observable.empty() }
}

fun <T> Observable<List<T>>.onErrorReturnEmptyList(): Observable<List<T>> {
    return this.onErrorReturnItem(emptyList())
}

fun String?.or(elseString: String): String = this ?: elseString

fun CharSequence?.nullOrEmpty(): Boolean = this == null || this.isEmpty()