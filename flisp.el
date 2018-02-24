;;; FLisp mode -*- lexical-binding: t -*-

(define-derived-mode flisp-mode lisp-mode "FLisp"
  "FLisp is a typed Lisp."
  (put 'returns 'lisp-indent-function 1))



;;; flisp.el ends here
