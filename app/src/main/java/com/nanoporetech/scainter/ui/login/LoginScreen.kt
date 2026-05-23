package com.nanoporetech.scainter.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.conf.AppConstants
import com.nanoporetech.scainter.ui.components.PrimaryButton
import com.nanoporetech.scainter.ui.components.PrimarySwitch
import com.nanoporetech.scainter.ui.theme.ScaInterAppTheme
import com.nanoporetech.scainter.ui.theme.ScaInterTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {},
    username: String = "",
    onUsernameChanged: (String) -> Unit = {},
    password: String = "",
    onPasswordChanged: (String) -> Unit = {},
    onForgottenPassword: () -> Unit = {},
    isLoginError: Boolean = false,
    rememberMe: Boolean = false,
    onRememberMeChange: (value: Boolean) -> Unit = {},
) {
    val largePadding = dimensionResource(R.dimen.padding_large)

    Box(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            // HEADER SECTION
            HeaderAndLogo(
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(largePadding))

            // WELCOME MESSAGE SECTION
            WelcomeMessage(
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(largePadding))

            // CREDENTIALS SECTION
            CredentialsSection(
                username = username,
                onUsernameChanged = onUsernameChanged,
                password = password,
                onPasswordChanged = onPasswordChanged,
                onLogin = onLogin,
                isError = isLoginError,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(largePadding))

            // FORGOTTEN PASSWORD SECTION
            ForgottenPasswordSection(
                rememberMe = rememberMe,
                onRememberMeChange = onRememberMeChange,
                onForgottenPassword = onForgottenPassword,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(largePadding))

            // LOGIN BUTTON
            PrimaryButton(
                text = stringResource(R.string.login),
                onClick = onLogin,
                enabled = username.isNotBlank() && password.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun HeaderAndLogo(
    modifier: Modifier = Modifier
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            //.border(1.dp, color = Color.Red)
    ) {
        Image(
            painter = painterResource(R.drawable.sca_logo_no_title),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_large))
        )

        Spacer(modifier = Modifier.width(mediumPadding))

        Text(
            text = stringResource(R.string.company_name),
            style = MaterialTheme.typography.displaySmall,
            color = ScaInterTheme.extendedColors.mainGreen.color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CredentialsSection(
    username: String,
    onUsernameChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier) {
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = username,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.username_label))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                disabledIndicatorColor =  MaterialTheme.colorScheme.outlineVariant,
            ),
            onValueChange = { onUsernameChanged(it) },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            visualTransformation = VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(dimensionResource(R.dimen.elevation_small))
        )

        OutlinedTextField(
            value = password,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.password_label)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                disabledIndicatorColor =  MaterialTheme.colorScheme.outlineVariant,
            ),
            onValueChange = { onPasswordChanged(it) },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = { onLogin() }
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(dimensionResource(R.dimen.elevation_small))
        )
    }
}

@Composable
fun WelcomeMessage(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.welcome_message),
        color = ScaInterTheme.extendedColors.mainGreen.color,
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun ForgottenPasswordSection(
    rememberMe: Boolean,
    onRememberMeChange: (value: Boolean) -> Unit,
    onForgottenPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            PrimarySwitch(
                checked = rememberMe,
                onCheckedChange = onRememberMeChange,
            )

            Text(
                text = stringResource(R.string.remember_me),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        }

        Text(
            text = stringResource(R.string.password_forgotten),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable(
                    //interactionSource = remember { MutableInteractionSource() },
                    //indication = LocalIndication.current,
                    onClick = onForgottenPassword
                )
        )
    }
}


@Preview(
    locale = "fr-rCI",
    showBackground = true)
@Composable
fun LoginScreenPreview() {
    ScaInterAppTheme {
        LoginScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(AppConstants.lightGreen)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}