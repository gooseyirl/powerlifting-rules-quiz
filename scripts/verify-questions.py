#!/usr/bin/env python3
"""Check every quiz question's ruleQuote actually appears on the page it cites.

Run from the repo root:  python3 scripts/verify-questions.py [questions.json]

Quotes may elide text with an ellipsis (…) and may carry editorial insertions
in [square brackets] — a label added to a table row, or a corrected word. Both
break the verbatim run, so the text between them must each be found on the
cited page. Page footers print at the END of their page in pdftotext output, so
never eyeball page numbers from a multi-page dump.
"""
import json, re, subprocess, sys, unicodedata

PDF = "rulesbooks/2026_IPF_Technical_Rulebook__effective_01_March_2026__v3.pdf"
SRC = sys.argv[1] if len(sys.argv) > 1 else "docs/questions.json"

def norm(s):
    s = unicodedata.normalize("NFKD", s)
    for a, b in [("’","'"), ("“",'"'), ("”",'"'), ("–","-"), ("—","-")]:
        s = s.replace(a, b)
    return re.sub(r"[^a-z0-9]+", " ", s.lower()).strip()

raw = subprocess.run(["pdftotext", "-layout", PDF, "-"],
                     capture_output=True, text=True).stdout
pages = {i: norm(p) for i, p in enumerate(raw.split("\f"), 1)}

questions = json.load(open(SRC))["questions"]
bad = 0

for q in questions:
    claimed = q["ruleReference"]["pageNumber"]
    # ellipses and [editorial insertions] both end a verbatim run
    parts = re.split(r"…|\.\.\.|\[[^\]]*\]", q["ruleQuote"])
    fragments = [norm(f) for f in parts if norm(f)]
    # a fragment must sit on one page; find pages holding ALL fragments
    hits = [sorted(p for p, t in pages.items() if f in t) for f in fragments]
    found = sorted(set.intersection(*[set(h) for h in hits])) if all(hits) else []
    ok = claimed in found

    if not ok:
        bad += 1
        missing = [fragments[n][:50] for n, h in enumerate(hits) if not h]
        detail = f"fragment not in PDF: '{missing[0]}...'" if missing else f"quote sits on {found or '(no single page)'}"
        print(f"Q{q['id']:<3} claimed p{claimed:<3} FAIL  {detail}")
    else:
        print(f"Q{q['id']:<3} claimed p{claimed:<3} ok"
              + (f"  (also on {[p for p in found if p != claimed]})" if len(found) > 1 else ""))

print(f"\n{len(questions)} questions, {bad} failing")
sys.exit(1 if bad else 0)
