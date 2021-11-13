package com.example.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentAllDishesBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.view.activities.AddUpdateDishActivity
import com.example.favdish.view.activities.MainActivity
import com.example.favdish.view.adapters.FavDishAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory

class AllDishesFragment : Fragment() {

    private val mFavDishViewModel:FavDishViewModel by viewModels{
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }
    private lateinit var mBinding: FragmentAllDishesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        mBinding = FragmentAllDishesBinding.inflate(inflater,container,false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(),2)
        val favDishAdapter=FavDishAdapter(this@AllDishesFragment)

        mBinding.rvDishesList.adapter =favDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner){ dishes ->

            dishes.let {
                if (it.isNotEmpty()) {

                    mBinding.rvDishesList.visibility = View.VISIBLE
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE

                    favDishAdapter.dishesList(it)
                } else {

                    mBinding.rvDishesList.visibility = View.GONE
                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dishDetails(favDish:FavDish){
        findNavController()
            .navigate(AllDishesFragmentDirections.actionAllDishesToDishDetails(favDish))

        if (requireActivity() is MainActivity){
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_all_dishes,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.action_add_dish ->{
                startActivity(Intent(requireActivity(),AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}