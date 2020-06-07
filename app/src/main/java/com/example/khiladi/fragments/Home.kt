package com.example.khiladi.fragments


import com.example.khiladi.Adapters.EventAdapter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentHomeBinding
import com.squareup.picasso.Picasso
import androidx.fragment.app.*
import com.example.khiladi.Adapters.ChooseTeamAdapter
import com.example.khiladi.DialogChooseSingleSportsCategory
import com.example.khiladi.Models.Event
import com.example.khiladi.Models.Notification
import com.example.khiladi.Models.Team
import com.example.khiladi.TimeAgo
import com.example.khiladi.createEvent.EventCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Suppress("DEPRECATION")
class Home : Fragment(), EventAdapter.ResponceListener,SportsSingleCategoriesAdapter.SportsSingleCategoryListener,
    ChooseTeamAdapter.TeamListener , EventCategory.BackToHome{

    lateinit var event : ArrayList<Event>
    lateinit var homebinding : FragmentHomeBinding
    private var all = ArrayList<Event>()
    private var fixtures = ArrayList<Event>()
    private var urgent = ArrayList<Event>()
    private lateinit var eventAdapter : EventAdapter
    private var respondedEvent =  Event()
    private var selectedTeam = Team()
    private var currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var selectedCategory = SportsCatergory("-M28tl8D-yXJYjhiMHOX","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS3-KtExMO-J4Ug7vRkfAja4G95a5JI1_bHXzGsJTva0fSMd2ni","cricket")


    override fun back(eventType : String) {
        if(eventType == "single"){
            val bundle = Bundle()
            bundle.putString("type","single")
            findNavController().navigate(R.id.action_home_to_sportsAndTeam,bundle)
        }
    }

    override fun selectedCategory(selectedSportsCategory: SportsCatergory) {

    }

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        selectedCategory = sportsCategory[0]
        Picasso.get().load(selectedCategory.photo).into(homebinding.sportsCategory)
        addData()
    }

    override fun callback(team: Team) {
        selectedTeam = team
        val responce = HashMap<String,String>()
        responce.put(selectedTeam.id.toString(),selectedTeam.id.toString())
        firebaseDatabase.getReference("events/${selectedCategory.title}/${respondedEvent.team1}/${respondedEvent.id}/responces").updateChildren(responce as Map<String, Any>).addOnSuccessListener {
            //Toast.makeText(context,"Responce send",Toast.LENGTH_SHORT).show()
            sendResponseNotification(respondedEvent.team1,selectedCategory.title)
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendResponseNotification(team1: String?, category: String) {
        firebaseDatabase.getReference("teams/$category/$team1/captainId").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val senderCaptainId = p0.value.toString()
                val ref  = firebaseDatabase.getReference("notification/$currentUser")
                val key = ref.push().key
                firebaseDatabase.getReference("teams/$category/$team1/profile").addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val profilePic = p0.value.toString()
                        firebaseDatabase.getReference("teams/$category/${respondedEvent.team1}/$senderCaptainId").addValueEventListener(object :ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                val captainId = p0.value.toString()
                                val tsLong = System.currentTimeMillis()
                                Log.d("selectedTeam",selectedTeam.id)
                                val notification = Notification(key,profilePic,tsLong," responded to your event",captainId,currentUser!!,"event response",selectedCategory.title,
                                    selectedTeam.id.toString(),
                                    false,
                                    false,"${respondedEvent.team1}/${respondedEvent.id}"
                                )
                               firebaseDatabase.getReference("notification/$currentUser/$key").setValue(notification).addOnSuccessListener {
                                    Toast.makeText(context,"Responce send",Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
                                }

                            }

                        })

                    }

                })

            }

        })
    }

    override fun sendResponce(event: Event) {
        respondedEvent = event
        Toast.makeText(context,respondedEvent.category,Toast.LENGTH_SHORT).show()
        dialogChooseTeamFragment(respondedEvent)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homebinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
         eventAdapter = EventAdapter(all,this)
        val tsLong = System.currentTimeMillis()
        Log.d("currentKhiladiTimeTime",tsLong.toString())
        Log.i("currentKhiladiTimeTime",tsLong.toString())
        addData()

        homebinding.newEvent.setOnClickListener {
            //findNavController().navigate(R.id.action_home_to_createEvent)
            val fragment = EventCategory()
            fragment.setTargetFragment(this,1)
            fragment.show(fragmentManager!!, "")
        }
        homebinding.AllEvents.setOnClickListener {
            homebinding.AllEvents.background = resources.getDrawable(R.drawable.chips)
            homebinding.myEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.fixtures.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.urgentEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.recyclerViewHome.adapter = EventAdapter(all,this)
        }
        homebinding.myEvents.setOnClickListener {
            homebinding.AllEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.myEvents.background = resources.getDrawable(R.drawable.chips)
            homebinding.fixtures.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.urgentEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.recyclerViewHome.adapter = EventAdapter(all,this)
        }
        homebinding.urgentEvents.setOnClickListener {
            homebinding.AllEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.myEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.fixtures.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.urgentEvents.background = resources.getDrawable(R.drawable.chips)
            homebinding.recyclerViewHome.adapter = EventAdapter(urgent,this)
        }
        homebinding.fixtures.setOnClickListener {
            homebinding.AllEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.myEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.fixtures.background = resources.getDrawable(R.drawable.chips)
            homebinding.urgentEvents.background = resources.getDrawable(R.drawable.chips_unfocus)
            homebinding.recyclerViewHome.adapter = EventAdapter(fixtures,this)
        }
        homebinding.notificationHome.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_notification)
        }
        homebinding.sportsCategory.setOnClickListener {
            dialogChooseCategoryFragment()
           /* if (BottomSheetBehavior.from(bottom_sheet).state != BottomSheetBehavior.STATE_EXPANDED) {
                BottomSheetBehavior.from(bottom_sheet).setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                BottomSheetBehavior.from(bottom_sheet).setState(BottomSheetBehavior.STATE_COLLAPSED)
            }*/
        }

        return homebinding.root
    }

    private fun dialogChooseCategoryFragment() {
        val fragment = DialogChooseSingleSportsCategory()
        fragment.setTargetFragment(this,1)
        fragment.show(fragmentManager!!, "")
    }

    private fun dialogChooseTeamFragment(respondedEvent: Event) {
        val fragment = DialogChooseTeam(respondedEvent)
        fragment.setTargetFragment(this,1)
        fragment.show(fragmentManager!!, "")
    }

    private fun addData() {
     /*   var team1 = Team("https://pbs.twimg.com/profile_images/929330290182508545/wnn986Jl_400x400.jpg","QG","Cricket","Close")
        var team2 = Team("https://i.pinimg.com/474x/61/4a/a0/614aa0e724704458997c8380d81843f4.jpg","ISLU","Cricket","Close")

        val uid1 = UUID.randomUUID().toString()
        val uid2 = UUID.randomUUID().toString()
        val uid3 = UUID.randomUUID().toString()
        val uid4 = UUID.randomUUID().toString()
        var uid = java.util.ArrayList<String>()
        uid.add(uid1)
        uid.add(uid2)
        uid.add(uid3)
        uid.add(uid4)

        val event1 = Event(uid1,team1,null,"Monday 4:30pm","21st jan 2020/",true,false,false,"Rawalpindi")
        val event2 = Event(uid2,team1,team2,"Monday 4:30pm","21st jan 2020/",false,false,true,"Rawalpindi")
        val event3 = Event(uid3,team1,null,"Monday 4:30pm","21st jan 2020/",false,false,false,"Rawalpindi")
        val event4 = Event(uid4,team1,team2,"Monday 4:30pm","21st jan 2020/",false,false,true,"Rawalpindi")


            firebaseDatabase.getReference("/event/$uid1").setValue(event1).addOnSuccessListener {
                Toast.makeText(context,"Added",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }

        firebaseDatabase.getReference("/event/$uid2").setValue(event2).addOnSuccessListener {
            Toast.makeText(context,"Added",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }

        firebaseDatabase.getReference("/event/$uid3").setValue(event3).addOnSuccessListener {
            Toast.makeText(context,"Added",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }

        firebaseDatabase.getReference("/event/$uid4").setValue(event4).addOnSuccessListener {
            Toast.makeText(context,"Added",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }

*/


        firebaseDatabase.getReference("events/${selectedCategory.title}").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                all.clear()
                urgent.clear()
                fixtures.clear()
                homebinding.recyclerViewHome.adapter = EventAdapter(all,this@Home)
                p0.children.forEach{
                    it?.children?.forEach{
                        val event = it?.getValue(Event::class.java)
                        all.add(event!!)
                        homebinding.recyclerViewHome.adapter = EventAdapter(all,this@Home)
                    }

                }
                all.forEach {
                    if (it.urgent == true){
                        urgent.add(it)
                    }
                    if (it.fixture == true){
                        fixtures.add(it)
                    }
                }
            }

        })
    }

}





