from utils import GetConfig
from utils import BorhanBaseTest

from BorhanClient.Plugins.Core import BorhanWidgetListResponse

class WidgetTests(BorhanBaseTest):
     
    def test_list_widgets(self):
        widgets = self.client.widget.list()
        self.assertIsInstance(widgets, BorhanWidgetListResponse)


import unittest
def test_suite():
    return unittest.TestSuite((
        unittest.makeSuite(WidgetTests),
        ))

if __name__ == "__main__":
    suite = test_suite()
    unittest.TextTestRunner(verbosity=2).run(suite)
    
    