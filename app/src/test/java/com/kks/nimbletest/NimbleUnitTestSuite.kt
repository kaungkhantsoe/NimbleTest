package com.kks.nimbletest

import com.kks.nimbletest.repo.forget.ForgetPasswordRepoImplTest
import com.kks.nimbletest.repo.home.HomeRepoImplTest
import com.kks.nimbletest.repo.login.LoginRepoImplTest
import com.kks.nimbletest.repo.token.TokenRepoImplTest
import com.kks.nimbletest.viewmodel.forget.ForgetPasswordViewModelTest
import com.kks.nimbletest.viewmodel.home.HomeViewModelTest
import com.kks.nimbletest.viewmodel.login.LoginViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Created by kaungkhantsoe at 20/12/2021
 */

@kotlinx.coroutines.ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginRepoImplTest::class,
    TokenRepoImplTest::class,
    HomeRepoImplTest::class,
    ForgetPasswordRepoImplTest::class,

    LoginViewModelTest::class,
    HomeViewModelTest::class,
    ForgetPasswordViewModelTest::class,
)
class NimbleUnitTestSuite
