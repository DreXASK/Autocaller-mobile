package com.drexask.autocaller.mobile.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedButtonWithProgress(
	onClick: () -> Unit,
	buttonText: @Composable () -> Unit,
) {
	Box(
		contentAlignment = Alignment.Center
	) {
		OutlinedButton(
			onClick = onClick
		) {
			buttonText()
		}
		CircularProgressIndicator(
			modifier = Modifier
				.width(256.dp)
				.aspectRatio(1f),
			color = MaterialTheme.colorScheme.secondary,
			strokeCap = StrokeCap.Round
		)
	}
}
