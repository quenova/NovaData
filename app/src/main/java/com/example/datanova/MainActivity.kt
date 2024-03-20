package com.example.datanova

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.datanova.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabaseKtxRegistrar

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null
    private val RC_SING_IN = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showData.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpety(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.save -> {
                val getUserId = auth!!.currentUser!!.uid

                val database = FirebaseDatabase.getInstance()

                val getNama: String = binding.nama.text.toString()
                val getAlamat: String = binding.alamat.text.toString()
                val getNoHP: String = binding.noHp.text.toString()

                val getReference: DatabaseReference
                getReference = database.reference

                if (isEmpety(getNama) || isEmpety(getAlamat) || isEmpety(getNoHP)) {
                    Toast.makeText(this@MainActivity, "Data tidak boleh kosong",
                    Toast.LENGTH_SHORT).show()
                }else{
                    getReference.child("Admin").child(getUserId).child("Data Nova").push()
                        .setValue(data_teman(getNama, getAlamat, getNoHP))
                        .addOnCompleteListener(this){

                            binding.nama.setText("")
                            binding.alamat.setText("")
                            binding.noHp.setText("")
                            Toast.makeText(this@MainActivity, "Data Terlampir", Toast.LENGTH_SHORT).show()

                        }
                }
            }
            R.id.logout -> {
                AuthUI.getInstance().signOut(this@MainActivity).addOnCompleteListener(object :
                    OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        Toast.makeText(this@MainActivity, "Logout Berhasil", Toast.LENGTH_SHORT)
                            .show()
                        intent = Intent(applicationContext, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                })
            }

            R.id.show_data -> {
            }
        }
    }
}