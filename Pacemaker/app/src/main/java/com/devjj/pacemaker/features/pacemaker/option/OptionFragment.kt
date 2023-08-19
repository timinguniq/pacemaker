package com.devjj.pacemaker.features.pacemaker.option


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.core.extension.loadColor
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.core.navigation.Navigator
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.databinding.FragmentOptionBinding
import com.devjj.pacemaker.features.pacemaker.OptionActivity
import javax.inject.Inject

class OptionFragment : BaseFragment() {

    private lateinit var optionViewModel: OptionViewModel
    @Inject
    lateinit var setting: SettingSharedPreferences
    @Inject
    lateinit var navigator: Navigator

    private var _binding: FragmentOptionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding by lazy {
        _binding!!
    }

    override fun layoutId() = R.layout.fragment_option

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(activity, getString(R.string.foption_toast_saved), Toast.LENGTH_SHORT).show()
        _binding = null
    }

    private fun initializeView() {
        setColors()

        binding.fOptionSwcModeItem.isChecked = setting.isNightMode
        binding.fOptionSwcModeWeight.isChecked = setting.isUpdateWeight
        binding.fOptionSwcModeHeight.isChecked = setting.isUpdateHeight

        val optionListener = OptionListener(activity!!, binding, setting, navigator, ::setColors)

        optionListener.initListener()
        binding.fOptionTvIntervalTime.text = getString(R.string.unit_time_min_sec,setting.restTime/60,setting.restTime%60)
    }

    private fun setColors() {
        when (setting.isNightMode) {
            true -> {
                Dlog.d( "Dark Mode = ${setting.isNightMode}")
                activity!!.window.statusBarColor = loadColor(activity!!,R.color.grey_444646)
                (requireActivity() as OptionActivity).getBinding()
                    .aOptionCloTitle.setBackgroundResource(R.drawable.img_title_background_nighttime)
                (requireActivity() as OptionActivity).getBinding()
                    .aOptionFloContainer.setBackgroundColor(loadColor(activity!!,R.color.grey_444646))

                binding.fOptionSwcModeItem.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                binding.fOptionSwcModeItem.trackDrawable.setTint(loadColor(activity!!,R.color.orange_FF765B))
                binding.fOptionSwcModeWeight.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                binding.fOptionSwcModeWeight.trackDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                binding.fOptionSwcModeHeight.thumbDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))
                binding.fOptionSwcModeHeight.trackDrawable.setTint(loadColor(activity!!,R.color.orange_F74938))

                binding.fOptionCloProfile.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOptionCloSupportUs.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOptionCloOption.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))

                binding.fOptionTvProfile.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvSupportUs.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvOption.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fOptionTvModeTitle.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fOptionTvIntervalTitle.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fOptionTvIntervalTime.setTextColor(loadColor(activity!!,R.color.grey_B2B2B2))
                binding.fOptionIvIntervalMinus.setImageResource(R.drawable.img_rest_minus_nighttime)
                binding.fOptionIvIntervalPlus.setImageResource(R.drawable.img_rest_plus_nighttime)

                binding.fOptionTvRateUs.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fOptionTvFeedback.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fOptionTvShare.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fOptionTvOpenSource.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fOptionTvModeTutorial.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fOptionTvModeWeight.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))
                binding.fOptionTvModeHeight.setTextColor(loadColor(activity!!,R.color.white_F7FAFD))

                binding.fOptionCloLine01.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOptionCloLine02.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOptionCloLine03.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOptionCloLine04.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOptionCloLine05.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))
                binding.fOptionCloLine06.setBackgroundColor(loadColor(activity!!,R.color.grey_606060))

                binding.fOptionTvUIDesignBy.setTextColor(loadColor(activity!!,R.color.grey_B2B2B2))
            }
            false -> {
                activity!!.window.statusBarColor = loadColor(activity!!,R.color.blue_5C83CF)
                (requireActivity() as OptionActivity).getBinding()
                    .aOptionCloTitle.setBackgroundResource(R.drawable.img_title_background_daytime)
                (requireActivity() as OptionActivity).getBinding()
                    .aOptionFloContainer.setBackgroundColor(loadColor(activity!!,R.color.white_FFFFFF))

                binding.fOptionSwcModeItem.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                binding.fOptionSwcModeItem.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))
                binding.fOptionSwcModeWeight.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                binding.fOptionSwcModeWeight.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))
                binding.fOptionSwcModeHeight.thumbDrawable.setTint(loadColor(activity!!,R.color.blue_5F87D6))
                binding.fOptionSwcModeHeight.trackDrawable.setTint(loadColor(activity!!,R.color.blue_5C83CF))

                binding.fOptionCloProfile.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOptionCloSupportUs.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOptionCloOption.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))

                binding.fOptionTvProfile.setTextColor(loadColor(activity!!,R.color.grey_87888A))
                binding.fOptionTvSupportUs.setTextColor(loadColor(activity!!,R.color.grey_87888A))
                binding.fOptionTvOption.setTextColor(loadColor(activity!!,R.color.grey_87888A))


                binding.fOptionTvIntervalTitle.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvIntervalTime.setTextColor(loadColor(activity!!,R.color.grey_B2B2B2))
                binding.fOptionIvIntervalMinus.setImageResource(R.drawable.img_rest_minus_daytime)
                binding.fOptionIvIntervalPlus.setImageResource(R.drawable.img_rest_plus_daytime)

                binding.fOptionTvModeTitle.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvRateUs.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvFeedback.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvShare.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvOpenSource.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvModeTutorial.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvModeWeight.setTextColor(loadColor(activity!!,R.color.black_3B4046))
                binding.fOptionTvModeHeight.setTextColor(loadColor(activity!!,R.color.black_3B4046))

                binding.fOptionCloLine01.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOptionCloLine02.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOptionCloLine03.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOptionCloLine04.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOptionCloLine05.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))
                binding.fOptionCloLine06.setBackgroundColor(loadColor(activity!!,R.color.grey_F9F9F9))

                binding.fOptionTvUIDesignBy.setTextColor(loadColor(activity!!,R.color.grey_87888A))
            }
        }
    }
}

