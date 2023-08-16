package com.example.mindfriendfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
//import com.example.mindfriendfront.category.AnalysisFragment
import com.example.mindfriendfront.databinding.ActivityNaviBinding

private const val TAG_ANALYSIS = "analysis_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_MY_PAGE = "my_page_fragment"


class NaviActivity : AppCompatActivity() {
    //뷰바인딩 사용
    private lateinit var binding: ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //네비 항목 클릭 시 프래그먼트 변경하는 함수 호출
        // TODO : setOnNavigationItemSelectedListener가 deprecated되어서 대체했는데 setOnItemReselectedListener 는 뭐가 다른 거지?
        binding.mainNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.analysisFragment -> setFragment(TAG_ANALYSIS, Analysis_fragment())
                R.id.homeFragment -> setFragment(TAG_HOME, Calendar_fragment())
                R.id.myPageFragment -> setFragment(TAG_MY_PAGE, DiaryRead_fragment())
            }
            true
        }

        //처음에는 홈 버튼이 선택되도록 설정
        binding.mainNavi.selectedItemId = R.id.homeFragment

    }

    //프래그먼트 컨트롤 함수
    fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        //트랜잭션에 tag로 전달된 fragment가 없을 경우 add
        if(manager.findFragmentByTag(tag) == null){
            ft.add(R.id.mainNaviFragmentContainer, fragment, tag)
        }

        //작업이 수월하도록 manager에 add되어있는 fragment들을 변수로 할당해둠
        val analysis = manager.findFragmentByTag(TAG_ANALYSIS)
        val home = manager.findFragmentByTag(TAG_HOME)
        val myPage = manager.findFragmentByTag(TAG_MY_PAGE)

        //모든 프래그먼트 hide
        if(analysis!=null){
            ft.hide(analysis)
        }
        if(home!=null){
            ft.hide(home)
        }
        if(myPage!=null){
            ft.hide(myPage)
        }

        //선택한 항목에 따라 그에 맞는 프래그먼트만 show
        if(tag == TAG_ANALYSIS){
            if(analysis!=null){
                ft.show(analysis)
            }
        }
        else if(tag == TAG_HOME){
            if(home!=null){
                ft.show(home)
            }
        }
        else if(tag == TAG_MY_PAGE){
            if(myPage!=null){
                ft.show(myPage)
            }
        }

        //마무리
        ft.commitAllowingStateLoss()
        //ft.commit()
    }//seFragment함수 끝
}