package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.text.format.Formatter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.duyp.androidutils.glide.loader.GlideLoader
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.inflate
import com.duyp.architecture.clean.android.powergit.or
import com.duyp.architecture.clean.android.powergit.ui.provider.color.ColorsProvider
import com.duyp.architecture.clean.android.powergit.ui.utils.ParseDateFormat
import com.duyp.architecture.clean.android.powergit.ui.widgets.LabelSpan
import com.duyp.architecture.clean.android.powergit.ui.widgets.SpannableBuilder
import java.text.NumberFormat

class RepoViewHolder private constructor(
        itemView: View,
        private val glideLoader: GlideLoader
): RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.title)
    private val date = itemView.findViewById<TextView>(R.id.date)
    private val stars = itemView.findViewById<TextView>(R.id.stars)
    private val forks = itemView.findViewById<TextView>(R.id.forks)
    private val language = itemView.findViewById<TextView>(R.id.language)
    private val size = itemView.findViewById<TextView>(R.id.size)
    private val des = itemView.findViewById<TextView>(R.id.tvDes)
    private val avatar: ImageView? = itemView.findViewById<ImageView>(R.id.imvAvatar)

    private var forked: String? = itemView.context.getString(R.string.forked)
    private var privateRepo: String? = itemView.context.getString(R.string.private_repo)
    private var forkColor: Int = itemView.context.resources.getColor(R.color.material_indigo_700)
    private var privateColor: Int = itemView.context.resources.getColor(R.color.material_grey_700)

    fun bind(repo: RepoEntity) {
        if (repo.fork) {
            title.text = SpannableBuilder.builder()
                    .append(" $forked ", LabelSpan(forkColor))
                    .append(" ")
                    .append(repo.name, LabelSpan(Color.TRANSPARENT))
        } else if (repo.private) {
            title.text = SpannableBuilder.builder()
                    .append(" $privateRepo ", LabelSpan(privateColor))
                    .append(" ")
                    .append(repo.name, LabelSpan(Color.TRANSPARENT))
        } else {
            title.text = repo.fullName
        }

        des.text = repo.description.or("No description")

        // boolean isOrg = repo.getOwner() != null && repo.getOwner().isOrganizationType();
        avatar?.let {
            it.visibility = View.VISIBLE
            glideLoader.loadImage(repo.ownerAvatarUrl, it)
        }

        val repoSize = if (repo.size > 0) repo.size * 1000 else repo.size
        size.text = Formatter.formatFileSize(size.context, repoSize)
        val numberFormat = NumberFormat.getNumberInstance()
        stars.text = numberFormat.format(repo.stargazersCount)
        forks.text = numberFormat.format(repo.forks)
        date.text = ParseDateFormat.getTimeAgo(repo.updatedAt)
        if (!TextUtils.isEmpty(repo.language)) {
            language.text = repo.language
            language.setTextColor(ColorsProvider.getColorAsColor(repo.language!!, language.context))
            language.visibility = View.VISIBLE
        } else {
            language.text = ""
            language.visibility = View.GONE
        }
    }

    companion object {
        // has avatar version
        fun instanceWithAvatar(parent: ViewGroup, glideLoader: GlideLoader) =
                RepoViewHolder(parent.inflate(R.layout.item_repo), glideLoader)

        // no avatar version
        fun instanceNoAvatar(parent: ViewGroup, glideLoader: GlideLoader) =
                RepoViewHolder(parent.inflate(R.layout.item_repo_no_image), glideLoader)
    }

}