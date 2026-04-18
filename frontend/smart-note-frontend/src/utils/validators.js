export const validators = {
  email: (value) => {
    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    return regex.test(value)
  },

  phone: (value) => {
    const regex = /^1[3-9]\d{9}$/
    return regex.test(value)
  },

  password: (value) => {
    return value && value.length >= 6 && value.length <= 20
  },

  notEmpty: (value) => {
    return value && value.trim().length > 0
  },

  minLength: (value, min) => {
    return value && value.length >= min
  },

  maxLength: (value, max) => {
    return !value || value.length <= max
  }
}

export const messages = {
  email: '请输入正确的邮箱格式',
  phone: '请输入正确的手机号格式（11位数字，以1开头）',
  password: '密码长度需在6-20位之间',
  required: '此字段为必填项',
  confirmPassword: '两次密码输入不一致'
}
