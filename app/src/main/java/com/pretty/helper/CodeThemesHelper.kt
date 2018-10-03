package com.pretty.helper

import com.duyp.architecture.clean.android.powergit.PowerGitApp
import com.duyp.architecture.clean.android.powergit.ui.helper.InputHelper
import com.duyp.architecture.clean.android.powergit.ui.helper.PrefGetter
import java.io.IOException

/**
 * Created by Kosh on 21 Jun 2017, 1:44 PM
 */

object CodeThemesHelper {


    val CODE_EXAMPLE = "class ThemeCodeActivity : BaseActivity<ThemeCodeMvp.View, ThemeCodePresenter>(), ThemeCodeMvp.View {\n" +
            "\n" +
            "    val spinner: AppCompatSpinner by bindView(R.id.themesList)\n" +
            "    val webView: PrettifyWebView by bindView(R.id.webView)\n" +
            "    val progress: ProgressBar? by bindView(R.id.readmeLoader)\n" +
            "\n" +
            "    override fun layout(): Int = R.layout.theme_code_layout\n" +
            "\n" +
            "    override fun isTransparent(): Boolean = false\n" +
            "\n" +
            "    override fun canBack(): Boolean = true\n" +
            "\n" +
            "    override fun isSecured(): Boolean = false\n" +
            "\n" +
            "    override fun providePresenter(): ThemeCodePresenter = ThemeCodePresenter()\n" +
            "\n" +
            "    @OnClick(R.id.done) fun onSaveTheme() {\n" +
            "        val theme = spinner.selectedItem as String\n" +
            "        PrefGetter.setCodeTheme(theme)\n" +
            "        setResult(Activity.RESULT_OK)\n" +
            "        finish()\n" +
            "    }\n" +
            "\n" +
            "    override fun onInitAdapter(list: List<String>) {\n" +
            "        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list)\n" +
            "    }\n" +
            "\n" +
            "    @OnItemSelected(R.id.themesList) fun onItemSelect() {\n" +
            "        val theme = spinner.selectedItem as String\n" +
            "        webView.setThemeSource(CodeThemesHelper.CODE_EXAMPLE, theme)\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
            "        super.onCreate(savedInstanceState)\n" +
            "        progress?.visibility = View.VISIBLE\n" +
            "        webView.setOnContentChangedListener(this)\n" +
            "        title = \"\"\n" +
            "        presenter.onLoadThemes()\n" +
            "    }\n" +
            "\n" +
            "    override fun onContentChanged(p: Int) {\n" +
            "        progress?.let {\n" +
            "            it.progress = p\n" +
            "            if (p == 100) it.visibility = View.GONE\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    override fun onScrollChanged(reachedTop: Boolean, scroll: Int) {}\n" +
            "}"

    fun listThemes(): List<String> {
        try {
            val list = PowerGitApp.getInstance().getAssets().list("highlight/styles/themes")
                    .map({ s -> "themes/$s" })
                    .toMutableList()
            list.add(0, "prettify.css")
            list.add(1, "prettify_dark.css")
            return list
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return emptyList()
    }

    fun getTheme(isDark: Boolean): String {
        val theme = PrefGetter.getCodeTheme()
        return if (InputHelper.isEmpty(theme) || !exists(theme)) {
            if (!isDark) "prettify.css" else "prettify_dark.css"
        } else theme
    }

    private fun exists(theme: String): Boolean {
        return listThemes().contains(theme)
    }
}
