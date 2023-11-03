package com.example.changecolorsapp.views.changecolor

import com.example.changecolorsapp.model.colors.NamedColor

data class NamedColorListItem(
    val namedColor: NamedColor,
    val isSelected: Boolean
)