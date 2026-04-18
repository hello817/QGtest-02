package com.qgtest.diary.dto.friendDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendVO {
    private Long id;
    private String username;
    private String avatar;
    private String motto;
    private String groupTag;
}
