package com.nagi.ddtools.ui.toolpage.tools.fanboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.gson.Gson
import com.nagi.ddtools.databinding.ActivityFanBoardBinding
import com.nagi.ddtools.ui.base.DdToolsBaseActivity

class FanBoardActivity : DdToolsBaseActivity() {
    private lateinit var binding: ActivityFanBoardBinding
    private val viewModel: FanBoardViewModel by viewModels()
    private val bundle: Bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFanBoardBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        viewModel.isRolling.observe(this) { isRolling ->
            binding.boardIsRoll.isChecked = isRolling
            if (!isRolling) {
                binding.boardRollWay.visibility = View.GONE
                binding.boardRollWayGroup.visibility = View.GONE
                binding.boardRollSpeed.visibility = View.GONE
                binding.boardSpeedSeekbar.visibility = View.GONE
            } else {
                binding.boardRollWay.visibility = View.VISIBLE
                binding.boardRollWayGroup.visibility = View.VISIBLE
                binding.boardRollSpeed.visibility = View.VISIBLE
                binding.boardSpeedSeekbar.visibility = View.VISIBLE
            }
        }
        initView()
    }

    private fun initView() {
        binding.boardColorPicker.setText("文字颜色：")
        binding.boardColorPickerBackground.setText("背景颜色：")
        binding.boardColorPickerBackground.setIsSingleCheck(true)
        binding.boardIsRoll.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleRolling()
        }
        binding.boardClick.setOnClickListener {
            val color = binding.boardColorPicker.getSelectedColors()
            bundle.putString("color", Gson().toJson(color))
            openBoard()
        }
    }

    private fun openBoard() {
        val intent = Intent(this, BoardActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}