package com.example.androidassestmentproject.presentation.UserList

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.androidassestmentproject.databinding.ActivityUserListBinding
import com.example.androidassestmentproject.presentation.common.UserAdapter
import com.example.androidassestmentproject.presentation.common.applySystemBarInsets
import com.example.androidassestmentproject.presentation.common.toMessageRes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private val viewModel: UserListViewModel by viewModels()

    private val userAdapter = UserAdapter { user ->
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarInsets()

        binding.rvUsers.adapter = userAdapter
        binding.btnRetry.setOnClickListener { viewModel.loadUsers() }

        viewModel.uiState.observe(this) { state -> render(state) }
    }

    private fun render(state: UserListUiState) {
        binding.progressBar.isVisible = state is UserListUiState.Loading
        binding.rvUsers.isVisible = state is UserListUiState.Success
        binding.tvCacheInfo.isVisible = state is UserListUiState.Success && state.fromCache
        binding.layoutMessage.isVisible = state is UserListUiState.Error

        when (state) {
            UserListUiState.Loading -> Unit
            is UserListUiState.Success -> userAdapter.submitList(state.users)
            is UserListUiState.Error -> binding.tvMessage.setText(state.errorType.toMessageRes())
        }
    }
}