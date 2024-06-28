/*
 * Copyright 2013 Jason Winnebeck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utils.gui.charts.utils;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

/**
 * EventHandlerManager manages a set of event handler registrations on a target, for which all of those registrations
 * can be activated or deactivated in a single step. It also keeps the handlers from being added more than once.
 *
 * @author Jason Winnebeck
 */
public class EventHandlerManager {

	private final Node target;
	private final List<Registration<? extends Event>> registrations;

	EventHandlerManager(
			final Node target) {

		this.target = target;
		registrations = new ArrayList<>();
	}

	public Node getTarget() {
		return target;
	}

	/**
	 * Adds an event registration, optionally adding it to the target immediately.
	 */
	<
			T extends Event> void addEventHandler(
					final boolean addImmediately,
					final EventType<T> type,
					final EventHandler<? super T> handler) {

		final Registration<T> registration = new Registration<>(type, handler);
		registrations.add(registration);
		if (addImmediately) {
			registration.addHandler();
		}
	}

	/**
	 * Adds an event registration, adding it to the target immediately.
	 */
	public <
			T extends Event> void addEventHandler(
					final EventType<T> type,
					final EventHandler<? super T> handler) {
		addEventHandler(true, type, handler);
	}

	/**
	 * Add all currently un-added handlers (this method will not re-add).
	 */
	void addAllHandlers() {

		for (final Registration<?> registration : registrations) {
			registration.addHandler();
		}
	}

	/**
	 * Remove all currently added handlers.
	 */
	void removeAllHandlers() {

		for (final Registration<?> registration : registrations) {
			registration.removeHandler();
		}
	}

	private class Registration<
			T extends Event> {

		private final EventType<T> type;
		private final EventHandler<? super T> handler;
		private boolean registered;

		Registration(
				final EventType<T> type,
				final EventHandler<? super T> handler) {

			if (type == null) {
				throw new IllegalArgumentException("type cannot be null");
			}
			if (handler == null) {
				throw new IllegalArgumentException("handler cannot be null");
			}

			this.type = type;
			this.handler = handler;
		}

		private void addHandler() {

			if (!registered) {
				target.addEventHandler(type, handler);
				registered = true;
			}
		}

		private void removeHandler() {

			if (registered) {
				target.removeEventHandler(type, handler);
				registered = false;
			}
		}
	}
}
