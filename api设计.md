- api设计

  - 用户操作这边有注册，登录，修改密码，修改名称，修改头像，修改/添加座右铭，

  - 用户操作路径（/api）

    - 注册：POST  /api/users

    - 登录：POST /api/session/login

    - 修改密码: PUT /api/users/{id}/password

    - 修改名称: PUT /api/users/{id}/username

    - 修改/添加头像： PUT /api/users/{id}/avatar

    - 修改/添加座右铭: PUT /api/users/{id}/motto

    - 搜索用户（这里做一下模糊搜索，对象参数是邮箱或者手机号,通过有没有'@'判断）： GET /api/users?key={user_info}

  - 笔记操作路径(/api/)

    - 上传笔记： POST /api/session/put

    - 修改/更新笔记: PUT /api/diarys/{note_id}

    - 更改笔记可见性: PUT /api/diarys/{note_id}/visibility

    - 逻辑删除笔记（回收站）:  PUT /api/diarys/{note_id}/delete

    - 永久删除笔记: DELETE  /api/diarys/{note_id}

  - 好友操作路径(/api/)
    - 