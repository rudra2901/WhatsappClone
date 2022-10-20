package com.gdsc.nitb.rudra.whatsappclone.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gdsc.nitb.rudra.whatsappclone.R
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.black15
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.gray20Bold
import io.github.farhanroy.cccp.CCPCountry
import io.github.farhanroy.cccp.getFlagMasterResID
import io.github.farhanroy.cccp.getLibraryMasterCountriesEnglish

/**
 * A country code picker. This is used to give the user an option to select a country for the
 * phone number or other details.
 *
 * NOTE: The code is being take from https://github.com/farhanroy/ComposeCountryCodePicker.
 * The code copyright is being reserved by the https://github.com/farhanroy/ComposeCountryCodePicker
 * creator itself.
 *
 * I have made some changes according to the theme I required. But the basic functionality is still
 * the same as https://github.com/farhanroy/ComposeCountryCodePicker.
 *
 * So I don't take any credit for the functionality and all the credit goes to
 * https://github.com/farhanroy/ComposeCountryCodePicker.
 *
 * The license link for https://github.com/farhanroy/ComposeCountryCodePicker
 * is https://github.com/farhanroy/ComposeCountryCodePicker/blob/main/LICENSE.
 *
 * If any more details is being required to make this more clear please reach out to me at
 * pradyotprksh4@gmail.com, will make the changes accordingly.
 */

@Composable
fun CountryCodePicker(pickedCountry: (CCPCountry) -> Unit) {
    val countryList: List<CCPCountry> = getLibraryMasterCountriesEnglish()
    val picked = remember { mutableStateOf(countryList[0]) }
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(false) }
            val dialogWidth = 250.dp
            val dialogHeight = 400.dp

            Row(
                Modifier.clickable { openDialog.value = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularResourceImage(
                    resourceId = getFlagMasterResID(picked.value),
                    size = 30
                )
                SizedBox(
                    width = 5
                )
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.image_description),
                    modifier = Modifier.size(15.dp),
                    tint = Color.Gray
                )
                Text(
                    "+${picked.value.phoneCode}",
                    style = gray20Bold
                )
            }

            if (openDialog.value) {
                Dialog(onDismissRequest = { openDialog.value = false }) {
                    Box(
                        Modifier
                            .size(dialogWidth, dialogHeight)
                            .background(Color.White)
                    ) {
                        LazyColumn {
                            items(countryList.size) { index ->
                                Row(
                                    Modifier
                                        .padding(
                                            horizontal = 18.dp,
                                            vertical = 18.dp
                                        )
                                        .clickable {
                                            pickedCountry(countryList[index])
                                            picked.value = countryList[index]
                                            openDialog.value = false
                                        }) {
                                    CircularResourceImage(
                                        resourceId = getFlagMasterResID(countryList[index]),
                                        size = 30
                                    )
                                    Text(
                                        countryList[index].name,
                                        style = black15,
                                        modifier = Modifier.padding(horizontal = 18.dp)
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}