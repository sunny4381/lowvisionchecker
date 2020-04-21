(function(selector) {
  const fullXPath = function(el) {
    const stack = [];
    while (el.parentNode != null) {
      let sibCount = 0;
      let sibIndex = 0;
      for (var i = 0; i < el.parentNode.childNodes.length; i++) {
        const sib = el.parentNode.childNodes[i];
        if (sib.nodeName == el.nodeName) {
          if (sib === el) {
            sibIndex = sibCount;
          }
          sibCount++;
        }
      }
      if (sibCount > 1) {
        stack.unshift(el.nodeName.toLowerCase() + '[' + (sibIndex + 1) + ']');
      } else {
        stack.unshift(el.nodeName.toLowerCase());
      }
      el = el.parentNode;
    }

    return stack.join("/");
  };

  const texts = function(el) {
    if (! el.hasChildNodes()) {
      return null;
    }

    const ret = [];
    el.childNodes.forEach(childEl => {
      if (childEl.nodeName.toLowerCase() === "#text") {
        if (childEl.nodeValue && childEl.nodeValue.trim().length > 0) {
          ret.push(childEl.nodeValue);
        }
      }
    });

    return ret;
  };

  // const traverseDescendantNode = function(el, callback) {
  //   if (! el.hasChildNodes()) {
  //     return;
  //   }
  //
  //   el.childNodes.forEach(childEl => {
  //     callback(callback);
  //     traverseDescendantNode(childEl, callback);
  //   });
  // };
  //
  // const descendantTextsWithBGImage = function(el) {
  //   const backgroudImage = el.style.backgroundImage;
  //   if (backgroudImage == null || "none" === backgroudImage.toLowerCase()) {
  //     return null;
  //   }
  //
  //   traverseDescendantNode(el, (node) => {
  //
  //   });
  // };

  let baseUrl = null;
  const baseNode = document.head.querySelector("base");
  if (baseNode) {
    baseUrl = baseNode.href;
  }
  if (!baseUrl) {
    baseUrl = location.href;
  }

  const makeHash = function(el) {
    return {
      xpath: fullXPath(el),
      tagName: el.nodeName.toLowerCase(),
      rect: el.getBoundingClientRect(),
      style: el.style,
      computedStyle: getComputedStyle(el),
      href: el.href ? new URL(el.href, baseUrl).href : null,
      texts: texts(el)
    };
  };

  const ret = Array.from(document.body.querySelectorAll(selector), (el) => makeHash(el));
  ret.unshift(makeHash(document.body));

  return JSON.stringify(ret);
})("li");
