package com.example.foundation

import com.example.foundation.model.Repository

interface BaseApplication {

    /**
     * The list of repositories that can be added to the fragment view-model constructors.
     */
    val repositories: List<Repository>
}