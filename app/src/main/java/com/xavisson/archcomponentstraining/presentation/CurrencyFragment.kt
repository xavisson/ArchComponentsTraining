package com.xavisson.archcomponentstraining.presentation

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.xavisson.archcomponentstraining.R


class CurrencyFragment : Fragment() {

    companion object {
        fun newInstance() = CurrencyFragment()
    }

    private val currencies = ArrayList<String>()
    private var fromCurrencySpinner: Spinner? = null
    private var toCurrencySpinner: Spinner? = null
    private var currencyEdit: EditText? = null
    private var convertButton: Button? = null
    private var currenciesAdapter: ArrayAdapter<String>? = null
    private var currencyFrom: String? = null
    private var currencyTo: String? = null

    private var currencyViewModel: CurrencyViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateSpinnerAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.currency_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        populateSpinnerAdapter()
    }

    override fun onDestroyView() {
        currencyViewModel?.unSubscribeViewModel()
        super.onDestroyView()
    }

    private fun initViewModel() {
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)
        currencyViewModel?.initLocalCurrencies()
    }

    private fun initUI() {
        fromCurrencySpinner = view?.findViewById(R.id.from_currency_spinner) as Spinner
        toCurrencySpinner = view?.findViewById(R.id.to_currency_spinner) as Spinner
        currencyEdit = view?.findViewById(R.id.currency_edit) as EditText
        convertButton = view?.findViewById(R.id.convert_button) as Button
        initSpinners()
        initConvertButton()
    }

    private fun initSpinners() {
        currenciesAdapter = ArrayAdapter(activity, R.layout.item_spinner, currencies)
        fromCurrencySpinner?.adapter = currenciesAdapter
        fromCurrencySpinner?.setSelection(0)
        toCurrencySpinner?.adapter = currenciesAdapter
        toCurrencySpinner?.setSelection(0)
    }

    private fun initConvertButton() {
        convertButton?.setOnClickListener { convert() }
    }

    private fun populateSpinnerAdapter() {
        currencyViewModel?.loadCurrencyList()?.observe(this, Observer { currencyList ->
            currencyList!!.forEach {
                currencies.add(it.code + "  " + it.country)
            }
            currenciesAdapter!!.setDropDownViewResource(R.layout.item_spinner);
            currenciesAdapter!!.notifyDataSetChanged()
        })
    }


    private fun convert() {
        val quantity = currencyEdit?.text.toString()
        currencyFrom = getCurrencyCode(fromCurrencySpinner?.selectedItem.toString())
        currencyTo = getCurrencyCode(toCurrencySpinner?.selectedItem.toString())
        val currencies = currencyFrom + "," + currencyTo
        if (quantity.isNotEmpty() && currencyFrom != currencyTo) {
            currencyViewModel?.getAvailableExchange(currencies)?.observe(this, Observer { availableExchange ->
                exchange(quantity.toDouble(), availableExchange!!.availableExchangesMap)
            })
        } else {
            Toast.makeText(activity, "Could not convert.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exchange(quantity: Double, availableExchangesMap: Map<String, Double>) {
        val exchangesKeys = availableExchangesMap.keys.toList()
        val exchangesValues = availableExchangesMap.values.toList()
        val fromCurrency = exchangesValues[0]
        val toCurrency = exchangesValues[1]
        val fromCurrencyKey = getCurrencyCodeResult(exchangesKeys[0])
        val toCurrencyKey = getCurrencyCodeResult(exchangesKeys[1])
        val usdExchange = quantity.div(fromCurrency)
        val exchangeResult = usdExchange.times(toCurrency)
        val result = quantity.toString() + " " + fromCurrencyKey + " = " + exchangeResult.format(4) + " " + toCurrencyKey
        showResult(result)
    }

    private fun showResult(result: String) {
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(context!!, R.style.AppCompatAlertDialogStyle)
        } else {
            builder = AlertDialog.Builder(context!!)
        }
        val setMessage = TextView(activity)
        setMessage.text = result
        setMessage.gravity = Gravity.CENTER_HORIZONTAL
        builder.setView(setMessage)
        builder.setTitle(getString(R.string.currency_converter))
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(R.drawable.ic_attach_money_black_24dp)
                .show()
    }

    private fun getCurrencyCode(currency: String) = currency.substring(0, 3)

    private fun getCurrencyCodeResult(currency: String) = currency.substring(3)

    private fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
}