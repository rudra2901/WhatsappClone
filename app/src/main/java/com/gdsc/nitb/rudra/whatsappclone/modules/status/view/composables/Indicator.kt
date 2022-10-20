package com.gdsc.nitb.rudra.whatsappclone.modules.status.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gdsc.nitb.rudra.whatsappclone.ui.composables.SizedBox
import com.gdsc.nitb.rudra.whatsappclone.ui.theme.Notification

/**
 * A composable which generates a indicator with [length]
 */

@Composable
fun Indicator(length: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
    ) {
        for (i in 0 until length) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .weight(1f)
                    .background(Notification)
                    .padding(top = 10.dp)
            )
            if (i != length - 1) {
                SizedBox(width = 5)
            }
        }
    }
}