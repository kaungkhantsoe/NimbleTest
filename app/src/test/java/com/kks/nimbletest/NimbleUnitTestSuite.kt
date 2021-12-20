package com.kks.nimbletest

import com.kks.nimbletest.repo.login.LoginRepoImplTest
import com.kks.nimbletest.repo.token.TokenRepoImplTest
import com.kks.nimbletest.viewmodel.LoginViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

@kotlinx.coroutines.ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginRepoImplTest::class,
    LoginViewModelTest::class,
    TokenRepoImplTest::class
)
class NimbleUnitTestSuite