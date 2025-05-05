package me.hanhyur.newstrafficanalyzer.domain.traffic.model;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Article {

    private final String articleId;

}
