package io.michalzuk.horton.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.michalzuk.horton.R
import kotlinx.android.synthetic.main.fragment_charts.*
import org.eazegraph.lib.models.BarModel
import org.eazegraph.lib.models.PieModel


class ChartsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_charts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        generateBarChart()
        generatePieChart()
    }


    private fun generateBarChart() {
        barchart.addBar(BarModel(2.3f, -0xedcbaa))
        barchart.addBar(BarModel(2f, -0xcbcbaa))
        barchart.addBar(BarModel(3.3f, -0xa9cbaa))
        barchart.addBar(BarModel(1.1f, -0x78c0aa))
        barchart.addBar(BarModel(2.7f, -0xa9480f))
        barchart.addBar(BarModel(2f, -0xcbcbaa))
        barchart.addBar(BarModel(0.4f, -0xe00b54))
        barchart.addBar(BarModel(4f, -0xe45b1a))

        barchart.startAnimation()
    }

    private fun generatePieChart() {
        piechart.addPieSlice(PieModel("Freetime", 15f, Color.parseColor("#FE6DA8")))
        piechart.addPieSlice(PieModel("Sleep", 25f, Color.parseColor("#56B7F1")))
        piechart.addPieSlice(PieModel("Work", 35f, Color.parseColor("#CDA67F")))
        piechart.addPieSlice(PieModel("Eating", 9f, Color.parseColor("#FED70E")))

        piechart.startAnimation()
    }
}
