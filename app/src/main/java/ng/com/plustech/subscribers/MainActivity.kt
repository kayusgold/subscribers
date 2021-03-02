package ng.com.plustech.subscribers

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ng.com.plustech.subscribers.database.Subscriber
import ng.com.plustech.subscribers.database.SubscriberDatabase
import ng.com.plustech.subscribers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this

        initRecyclerview()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerview() {
        binding.subscriberRecyclerview.layoutManager = LinearLayoutManager(this)
        displaySubscribers()
    }

    private fun displaySubscribers() {
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.e("MYTAG", it.toString())
            binding.subscriberRecyclerview.adapter = RecyclerViewAdapter(it) { selectedItem: Subscriber ->
                listItemClicked(
                    selectedItem
                )
            }
        })
    }

    private fun listItemClicked(subscriber: Subscriber) {
        //Toast.makeText(this, "selected name is ${subscriber.name}", Toast.LENGTH_SHORT).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}