package com.example.androidassestmentproject.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.androidassestmentproject.R
import com.example.androidassestmentproject.databinding.ActivityUserDetailBinding
import com.example.androidassestmentproject.domain.model.UserDetail
import com.example.androidassestmentproject.presentation.common.applySystemBarInsets
import com.example.androidassestmentproject.presentation.common.toMessageRes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarInsets()

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnRetry.setOnClickListener { viewModel.loadUserDetail() }

        viewModel.uiState.observe(this) { state -> render(state) }
    }

    private fun render(state: UserDetailUiState) {
        binding.progressBar.isVisible = state is UserDetailUiState.Loading
        binding.contentLayout.isVisible = state is UserDetailUiState.Success
        binding.layoutMessage.isVisible = state is UserDetailUiState.Error

        when (state) {
            UserDetailUiState.Loading -> Unit
            is UserDetailUiState.Success -> {
                binding.tvCacheInfo.isVisible = state.fromCache
                bindUserDetail(state.userDetail)
            }

            is UserDetailUiState.Error -> binding.tvMessage.setText(state.errorType.toMessageRes())
        }
    }

    private fun bindUserDetail(user: UserDetail) {
        Glide.with(binding.ivAvatar)
            .load(user.avatarUrl)
            .placeholder(R.drawable.bg_avatar_placeholder)
            .circleCrop()
            .into(binding.ivAvatar)

        binding.tvName.text = user.name ?: user.username
        binding.tvUsername.text = getString(R.string.username_format, user.username)
        binding.tvRepos.text = getString(R.string.stat_repos, user.publicRepos)
        binding.tvFollowers.text = getString(R.string.stat_followers, user.followers)
        binding.tvFollowing.text = getString(R.string.stat_following, user.following)

        binding.tvBio.setTextOrHide(user.bio)
        binding.tvCompany.setTextOrHide(user.company, R.string.company_format)
        binding.tvLocation.setTextOrHide(user.location, R.string.location_format)
        binding.tvBlog.setTextOrHide(user.blog, R.string.blog_format)
    }

    private fun TextView.setTextOrHide(value: String?, @StringRes format: Int? = null) {
        isVisible = !value.isNullOrBlank()
        if (value.isNullOrBlank()) return
        text = if (format != null) getString(format, value) else value
    }

    companion object {
        fun newIntent(context: Context, username: String): Intent =
            Intent(context, UserDetailActivity::class.java)
                .putExtra(UserDetailViewModel.KEY_USERNAME, username)
    }
}