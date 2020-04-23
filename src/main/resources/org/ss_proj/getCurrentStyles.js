(function(selector) {
  const fullXPath = function(el) {
    const stack = [];
    while (el.parentNode != null) {
      let sibCount = 0;
      let sibIndex = 0;
      for (var i = 0; i < el.parentNode.childNodes.length; i++) {
        const sib = el.parentNode.childNodes[i];
        if (sib.nodeName === el.nodeName) {
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

    // https://code.jquery.com/jquery-3.3.1.js
  var escapeSelector = function(sel) { // $.escapeSelector()
    var rcssescape = /([\0-\x1f\x7f]|^-?\d)|^-$|[^\0-\x1f\x7f-\uFFFF\w-]/g;
    var fcssescape = function(ch, asCodePoint) {
      if (asCodePoint) {
        // U+0000 NULL becomes U+FFFD REPLACEMENT CHARACTER
        if (ch === "\0") {
          return "\uFFFD";
        }
        // Control characters and (dependent upon position) numbers get escaped as code points
        return ch.slice(0, -1) + "\\" + ch.charCodeAt(ch.length - 1).toString(16) + " ";
      }
      // Other potentially-special ASCII characters get backslash-escaped
      return "\\" + ch;
    };

    return (sel + '').replace(rcssescape, fcssescape);
  };

  const cssPath = function(el) {
    if (!(el instanceof Element)) {
      return;
    }

    const paths = [];
    while (el.nodeType === Node.ELEMENT_NODE) {
      let nodeName = el.nodeName.toLowerCase();
      if (el.id && document.querySelectorAll("#" + escapeSelector(el.id)).length === 1) {
        paths.unshift(`${nodeName}#${escapeSelector(el.id)}`);
        break;
      }

      let sib = el, nth = 1;
      while (sib = sib.previousElementSibling) {
        if (sib.nodeName.toLowerCase() === nodeName) {
          nth++;
        }
      }
      paths.unshift(nth !== 1 ? `${nodeName}:nth-of-type(${nth})` : nodeName);
      el = el.parentNode;
    }
    return paths.join(" > ");
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

  const sliceMap = function(obj, allowedKeys) {
    const ret = {};

    allowedKeys.forEach((key) => {
      ret[key] = obj[key];
    });

    return ret;
  }

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
      cssPath: cssPath(el),
      tagName: el.nodeName.toLowerCase(),
      rect: sliceMap(el.getBoundingClientRect(), [ "x", "y", "width", "height", "left", "top", "right", "bottom" ]),
      // style: el.style,
      computedStyle: sliceMap(
        getComputedStyle(el),
        [
          "background", "backgroundColor", "backgroundImage", "backgroundRepeat", "color", "display", "fontFamily",
          "fontSize", "fontStyle", "fontVariant", "letterSpacing", "lineHeight", "opacity", "position", "textAlign",
          "textDecoration", "visibility"
        ]
      ),
      href: el.href ? new URL(el.href, baseUrl).href : null,
      texts: texts(el)
    };
  };

  const ret = Array.from(document.body.querySelectorAll(selector), (el) => makeHash(el));
  ret.unshift(makeHash(document.body));

  return ret;
})("*");
