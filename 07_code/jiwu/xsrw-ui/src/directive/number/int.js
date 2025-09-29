export default {
  // bind绑定 获取当前目标的input（就是在哪个文本框上使用指令）
  bind: function(el) {
    const input = el.getElementsByTagName('input')[0];
    if (input) {
      // 调用input方法，加入正则校验的逻辑
      input.onkeyup = function(e) {
        if (input.value.length === 1) {
          input.value = input.value.replace(/[^1-9]/g, '');
        } else {
          input.value = input.value.replace(/[^\d]/g, '');
        }
        // 调用自定义事件
        trigger(input, 'input');
      };
      input.onblur = function(e) {
        if (input.value.length === 1) {
          input.value = input.value.replace(/[^1-9]/g, '');
        } else {
          input.value = input.value.replace(/[^\d]/g, '');
        }
        trigger(input, 'input');
      };
    }
  }
}

// 创建自定义事件
const trigger = (el, type) => {
  const e = document.createEvent('HTMLEvents');
  // 初始化默认值
  e.initEvent(type, true, true);
  // 触发自定义事件
  el.dispatchEvent(e);
};