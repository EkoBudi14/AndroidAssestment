package com.example.androidassestmentproject.presentation.userlist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.androidassestmentproject.R
import com.example.androidassestmentproject.databinding.ActivityUserListBinding
import com.example.androidassestmentproject.presentation.common.UserAdapter
import com.example.androidassestmentproject.presentation.common.applySystemBarInsets
import com.example.androidassestmentproject.presentation.common.showDataBanner
import com.example.androidassestmentproject.presentation.common.toMessageRes
import com.example.androidassestmentproject.presentation.detail.UserDetailActivity
import com.example.androidassestmentproject.presentation.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private val viewModel: UserListViewModel by viewModels()

    private val userAdapter = UserAdapter { user ->
        startActivity(UserDetailActivity.newIntent(this, user.username))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarInsets()
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    startActivity(SearchActivity.newIntent(this))
                    true
                }

                else -> false
            }
        }
        binding.rvUsers.adapter = userAdapter
        binding.btnRetry.setOnClickListener { viewModel.loadUsers() }
        viewModel.uiState.observe(this) { state ->
            render(state)
            renderBanner()
        }
        viewModel.isOnline.observe(this) { renderBanner() }
    }

    private fun render(state: UserListUiState) {
        binding.progressBar.isVisible = state is UserListUiState.Loading
        binding.rvUsers.isVisible = state is UserListUiState.Success
        binding.layoutMessage.isVisible = state is UserListUiState.Error

        when (state) {
            UserListUiState.Loading -> Unit
            is UserListUiState.Success -> userAdapter.submitList(state.users)
            is UserListUiState.Error -> binding.tvMessage.setText(state.errorType.toMessageRes())
        }
    }

    private fun renderBanner() {
        val isOnline = viewModel.isOnline.value ?: true
        val isShowingSavedData =
            (viewModel.uiState.value as? UserListUiState.Success)?.fromCache == true
        binding.tvBanner.showDataBanner(isOnline, isShowingSavedData)
    }
}