/*
 * Copyright (C) 2016 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pwittchen.reactivenetwork.library;

import com.github.pwittchen.reactivenetwork.library.internet.observing.strategy.DefaultInternetObservingStrategy;
import com.github.pwittchen.reactivenetwork.library.internet.socket.SocketErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import rx.Observable;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class) @Config(constants = BuildConfig.class)
public class DefaultInternetObservingStrategyTest {

  private final int initialIntervalInMs = 0;
  private final int intervalInMs = 2000;
  private final String host = "www.google.com";
  private final int port = 80;
  private final int timeoutInMs = 30;
  private @Mock SocketErrorHandler socketErrorHandler;

  @Test public void shouldBeConnectedToTheInternet() {
    // given
    SocketErrorHandler socketErrorHandler = mock(SocketErrorHandler.class);
    DefaultInternetObservingStrategy strategy = spy(new DefaultInternetObservingStrategy());
    when(strategy.isConnected(host, port, timeoutInMs, socketErrorHandler)).thenReturn(true);

    // when
    Observable<Boolean> observable =
        strategy.observeInternetConnectivity(initialIntervalInMs, intervalInMs, host, port,
            timeoutInMs, socketErrorHandler);

    boolean isConnected = observable.toBlocking().first();

    // then
    assertThat(isConnected).isTrue();
  }

  @Test public void shouldNotBeConnectedToTheInternet() {
    // given

    SocketErrorHandler socketErrorHandler = mock(SocketErrorHandler.class);
    DefaultInternetObservingStrategy strategy = spy(new DefaultInternetObservingStrategy());
    when(strategy.isConnected(host, port, timeoutInMs, socketErrorHandler)).thenReturn(false);

    // when
    Observable<Boolean> observable =
        strategy.observeInternetConnectivity(initialIntervalInMs, intervalInMs, host, port,
            timeoutInMs, socketErrorHandler);

    boolean isConnected = observable.toBlocking().first();

    // then
    assertThat(isConnected).isFalse();
  }
}