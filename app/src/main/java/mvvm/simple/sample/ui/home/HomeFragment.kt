package mvvm.simple.sample.ui.home


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil.inflate
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*
import mvvm.simple.sample.data.model.FoodDto
import mvvm.simple.sample.databinding.FragmentHomeBinding
import mvvm.simple.sample.R

class HomeFragment : Fragment(){
    private val TAG: String = HomeFragment::class.java.simpleName
    companion object {
        val FRAGMENT_NAME: String = HomeFragment::class.java.name
    }

    private val viewModel: HomeViewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }
    val adapter : HomeAdapter by lazy { HomeAdapter(mutableListOf()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentHomeBinding = inflate(inflater, R.layout.fragment_home, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            homeData.observe(this@HomeFragment, Observer {
                Toast.makeText(context,"toast shows in every rotation",Toast.LENGTH_SHORT).show()
                initView(it)
            }
            )
            showToast.observe(this@HomeFragment, Observer {
                Toast.makeText(context,"$it",Toast.LENGTH_LONG).show()

            })
            error.observe(this@HomeFragment, Observer {
                progressBar_home.visibility= View.GONE
                Toast.makeText(context, "$it", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun initView(it: FoodDto?) {
        rv_main_home.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_main_home.adapter = adapter
        progressBar_home.visibility= View.GONE
        if (it!!.results.isNotEmpty()) {
            adapter.clear()
            adapter.add(it.results)

        }else{
            Toast.makeText(context, context?.getString(R.string.empty_list), android.widget.Toast.LENGTH_LONG).show()
        }
    }
}