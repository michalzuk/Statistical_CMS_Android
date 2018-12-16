package io.michalzuk.horton.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.michalzuk.horton.R
import kotlinx.android.synthetic.main.fragment_contact_us.*
import android.content.Intent


class ContactUsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        send_contact_message_buttton.setOnClickListener {
            val name: String = contact_name__value.toString().trim()
            val email: String = contact_email_value.toString().trim()
            val subject: String = contact_subject_value.toString().trim()
            val message: String = contact_message_value.toString().trim()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(message) || TextUtils.isEmpty(email)
                    || TextUtils.isEmpty(subject)) {
                Snackbar.make(view!!.findViewById(R.id.fragment_contact_us), R.string.required_field_is_empty, Snackbar.LENGTH_SHORT).show()
            } else {
                val emailIntent = Intent(android.content.Intent.ACTION_SEND)

                /* Fill it with Data */
                emailIntent.type = "message/rfc822"
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("87339@stud.uz.zgora.pl"))
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Your Name:" + name + '\n'.toString() + "Email ID:" + email + '\n'.toString() + "Message:" + '\n'.toString() + message)

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))

            }
        }
    }
}
