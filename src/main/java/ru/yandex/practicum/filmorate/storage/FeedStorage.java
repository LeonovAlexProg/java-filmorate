package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Feed;

import java.time.Instant;
import java.util.Collection;

public interface FeedStorage {

    public Collection<Feed> getFeedList(int id);

    public void updateFeed(String eventType, String operation, int userId, int entityId, Instant instant);

}
