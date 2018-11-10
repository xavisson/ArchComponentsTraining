package com.xavisson.archcomponentstraining.presentation

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.xavisson.archcomponentstraining.R


class ConversionFragment : Fragment() {

    companion object {
        fun newInstance() = ConversionFragment()
    }

    private var fromCurrencySpinner: Spinner? = null
    private var toCurrencySpinner: Spinner? = null
    private var fromCurrencyEdit: EditText? = null
    private var toCurrencyEdit: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.conversion_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        fromCurrencySpinner = view?.findViewById(R.id.from_currency_spinner) as Spinner
        toCurrencySpinner = view?.findViewById(R.id.to_currency_spinner) as Spinner
        fromCurrencyEdit = view?.findViewById(R.id.from_currency_edit) as EditText
        toCurrencyEdit = view?.findViewById(R.id.to_currency_edit) as EditText
        populateSpinners()
        exchange()
    }

    private fun populateSpinners() {
        val currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel::class.java)
        val currencies = ArrayList<String>()
        val currenciesAdapter = ArrayAdapter(activity, R.layout.item_spinner, currencies)
        currencyViewModel.getCurrencyList()?.observe(this, Observer { currencyList ->
            currencyList!!.forEach {
                currencies.add(it.code + "  " + it.country)
            }
            currenciesAdapter.setDropDownViewResource(R.layout.item_spinner);
            currenciesAdapter.notifyDataSetChanged()
        })

        fromCurrencySpinner?.adapter = currenciesAdapter
        fromCurrencySpinner?.setSelection(0)
        toCurrencySpinner?.adapter = currenciesAdapter
        toCurrencySpinner?.setSelection(0)

    }


    private fun exchange() {

        fromCurrencyEdit?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                convert()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {
            }

        })
    }

    private fun convert() {
        val exchangeViewModel = ViewModelProviders.of(this).get(ExchangeViewModel::class.java)
        var currencyFrom = fromCurrencySpinner?.selectedItem.toString()
        val currencyTo = toCurrencySpinner?.selectedItem.toString()
        val currencies = getCurrencyCode(currencyFrom) + "," + getCurrencyCode(currencyTo)

        if (currencyFrom != currencyTo) {
            exchangeViewModel.getAvailableExchange(currencies)?.observe(this, Observer { availableExchange ->
                availableExchange?.availableExchangesMap?.forEach { key, value ->
                    val hola = key + " " + value
                    Toast.makeText(activity, hola, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getCurrencyCode(currency: String) = currency.substring(0, 3)
}
