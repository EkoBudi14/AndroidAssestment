package com.example.androidassestmentproject.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.example.androidassestmentproject.R
import com.example.androidassestmentproject.databinding.ActivitySearchBinding
import com.example.androidassestmentproject.presentation.common.UserAdapter
import com.example.androidassestmentproject.presentation.common.applySystemBarInsets
import com.example.androidassestmentproject.presentation.common.showDataBanner
import com.example.androidassestmentproject.presentation.common.toMessageRes
import com.example.androidassestmentproject.presentation.detail.UserDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private val userAdapter = UserAdapter { user ->
        startActivity(UserDetailActivity.newIntent(this, user.username))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarInsets()

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rvUsers.adapter = userAdapter
        binding.btnRetry.setOnClickListener { viewModel.retry() }
        setupSearchView()

        viewModel.uiState.observe(this) { state ->
            render(state)
            renderBanner()
        }
        viewModel.isOnline.observe(this) { renderBanner() }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })
    }

    private fun render(state: SearchUiState) {
        binding.progressBar.isVisible = state is SearchUiState.Loading
        binding.rvUsers.isVisible = state is SearchUiState.Success
        binding.layoutMessage.isVisible = state is SearchUiState.Idle ||
                state is SearchUiState.Empty ||
                state is SearchUiState.Error
        binding.btnRetry.isVisible = state is SearchUiState.Error

        when (state) {
            SearchUiState.Idle -> binding.tvMessage.setText(R.string.search_idle_message)
            SearchUiState.Empty -> binding.tvMessage.setText(R.string.search_empty_message)
            SearchUiState.Loading -> Unit
            is SearchUiState.Success -> userAdapter.submitList(state.users)
            is SearchUiState.Error -> binding.tvMessage.setText(state.errorType.toMessageRes())
        }
    }

    private fun renderBanner() {
        val isOnline = viewModel.isOnline.value ?: true
        val isShowingSavedData =
            (viewModel.uiState.value as? SearchUiState.Success)?.fromCache == true
        binding.tvBanner.showDataBanner(isOnline, isShowingSavedData)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SearchActivity::class.java)
    }
}