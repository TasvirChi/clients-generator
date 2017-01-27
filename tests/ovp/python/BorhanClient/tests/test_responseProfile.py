import uuid
import unittest

from utils import GetConfig
from utils import BorhanBaseTest
from utils import getTestFile

from BorhanClient.Plugins.Core import BorhanFilterPager, BorhanResponseProfile, BorhanResponseProfileMapping, BorhanDetachedResponseProfile, BorhanResponseProfileHolder, BorhanResponseProfileType
from BorhanClient.Plugins.Core import BorhanMediaEntry, BorhanMediaEntryFilter, BorhanBaseEntryListResponse, BorhanMediaType, BorhanEntryStatus
from BorhanClient.Plugins.Metadata import BorhanMetadata, BorhanMetadataProfile, BorhanMetadataFilter, BorhanMetadataListResponse, BorhanMetadataObjectType



class ResponseProfileTests(BorhanBaseTest):

    def setUp(self):
        BorhanBaseTest.setUp(self)
        self.uniqueTag = self.uniqid('tag_')
            
    def uniqid(self, prefix):
        return prefix + uuid.uuid1().hex

    def createEntry(self):
        entry = BorhanMediaEntry()
        entry.mediaType = BorhanMediaType.VIDEO
        entry.name = self.uniqid('test_')
        entry.description = self.uniqid('test ')
        entry.tags = self.uniqueTag

        entry = self.client.media.add(entry)

        return entry

    def createMetadata(self, metadataProfileId, objectType, objectId, xmlData):
        metadata = BorhanMetadata()
        metadata.metadataObjectType = objectType
        metadata.objectId = objectId

        metadata = self.client.metadata.metadata.add(metadataProfileId, objectType, objectId, xmlData)

        return metadata

    def createMetadataProfile(self, objectType, xsdData):
        metadataProfile = BorhanMetadataProfile()
        metadataProfile.metadataObjectType = objectType
        metadataProfile.name = self.uniqid('test_')
        metadataProfile.systemName = self.uniqid('test_')

        metadataProfile = self.client.metadata.metadataProfile.add(metadataProfile, xsdData)

        return metadataProfile

    def createEntriesWithMetadataObjects(self, entriesCount, metadataProfileCount = 2):
        entries = []
        metadataProfiles = {}

        for i in range(1,metadataProfileCount + 1):
            index = str(i)
            xsd = """<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">
    <xsd:element name=\"metadata\">
        <xsd:complexType>
            <xsd:sequence>
                <xsd:element name=\"Choice""" + index + """\" minOccurs=\"0\" maxOccurs=\"1\">
                    <xsd:annotation>
                        <xsd:documentation></xsd:documentation>
                        <xsd:appinfo>
                            <label>Example choice """ + index + """</label>
                            <key>choice""" + index + """</key>
                            <searchable>true</searchable>
                            <description>Example choice """ + index + """</description>
                        </xsd:appinfo>
                    </xsd:annotation>
                    <xsd:simpleType>
                        <xsd:restriction base=\"listType\">
                            <xsd:enumeration value=\"on\" />
                            <xsd:enumeration value=\"off\" />
                        </xsd:restriction>
                    </xsd:simpleType>
                </xsd:element>
                <xsd:element name=\"FreeText""" + index + """\" minOccurs=\"0\" maxOccurs=\"1\" type=\"textType\">
                    <xsd:annotation>
                        <xsd:documentation></xsd:documentation>
                        <xsd:appinfo>
                            <label>Free text """ + index + """</label>
                            <key>freeText""" + index + """</key>
                            <searchable>true</searchable>
                            <description>Free text """ + index + """</description>
                        </xsd:appinfo>
                    </xsd:annotation>
                </xsd:element>
            </xsd:sequence>
        </xsd:complexType>
    </xsd:element>
    <xsd:complexType name=\"textType\">
        <xsd:simpleContent>
            <xsd:extension base=\"xsd:string\" />
        </xsd:simpleContent>
    </xsd:complexType>
    <xsd:complexType name=\"objectType\">
        <xsd:simpleContent>
            <xsd:extension base=\"xsd:string\" />
        </xsd:simpleContent>
    </xsd:complexType>
    <xsd:simpleType name=\"listType\">
        <xsd:restriction base=\"xsd:string\" />
    </xsd:simpleType>
</xsd:schema>"""

            metadataProfiles[index] = self.createMetadataProfile(BorhanMetadataObjectType.ENTRY, xsd)

        for i in range(0, entriesCount):
            entry = self.createEntry()
            entries.append(entry)

            for j in range(1, metadataProfileCount + 1):
                index = str(j)
                xml = """<metadata>
    <Choice""" + index + """>on</Choice""" + index + """>
    <FreeText""" + index + """>example text """ + index + """</FreeText""" + index + """>
</metadata>"""

                self.createMetadata(metadataProfiles[index].id, BorhanMetadataObjectType.ENTRY, entry.id, xml)

        return [entries, metadataProfiles]
    
    def test_list(self):

        entriesTotalCount = 4
        entriesPageSize = 3
        metadataPageSize = 2

        entries, metadataProfiles = self.createEntriesWithMetadataObjects(entriesTotalCount, metadataPageSize)

        entriesFilter = BorhanMediaEntryFilter()
        entriesFilter.tagsLike = self.uniqueTag
        entriesFilter.statusIn = BorhanEntryStatus.PENDING + ',' + BorhanEntryStatus.NO_CONTENT

        entriesPager = BorhanFilterPager()
        entriesPager.pageSize = entriesPageSize

        metadataFilter = BorhanMetadataFilter()
        metadataFilter.metadataObjectTypeEqual = BorhanMetadataObjectType.ENTRY

        metadataMapping = BorhanResponseProfileMapping()
        metadataMapping.filterProperty = 'objectIdEqual'
        metadataMapping.parentProperty = 'id'

        metadataPager = BorhanFilterPager()
        metadataPager.pageSize = metadataPageSize

        metadataResponseProfile = BorhanDetachedResponseProfile()
        metadataResponseProfile.name = self.uniqid('test_')
        metadataResponseProfile.type = BorhanResponseProfileType.INCLUDE_FIELDS
        metadataResponseProfile.fields = 'id,objectId,createdAt, xml'
        metadataResponseProfile.filter = metadataFilter
        metadataResponseProfile.pager = metadataPager
        metadataResponseProfile.mappings = [metadataMapping]

        responseProfile = BorhanResponseProfile()
        responseProfile.name = self.uniqid('test_')
        responseProfile.systemName = self.uniqid('test_')
        responseProfile.type = BorhanResponseProfileType.INCLUDE_FIELDS
        responseProfile.fields = 'id,name,createdAt'
        responseProfile.relatedProfiles = [metadataResponseProfile]

        responseProfile = self.client.responseProfile.add(responseProfile)

        nestedResponseProfile = BorhanResponseProfileHolder()
        nestedResponseProfile.id = responseProfile.id

        self.client.setResponseProfile(nestedResponseProfile)
        list = self.client.baseEntry.list(entriesFilter, entriesPager)

        self.assertIsInstance(list, BorhanBaseEntryListResponse)
        self.assertEqual(entriesTotalCount, list.totalCount)
        self.assertEqual(entriesPageSize, len(list.objects))
        [self.assertIsInstance(entry, BorhanMediaEntry) for entry in list.objects]
        
        for entry in list.objects:
            self.assertNotEqual(entry.relatedObjects, NotImplemented)
            self.assertTrue(entry.relatedObjects.has_key(metadataResponseProfile.name))
            metadataList = entry.relatedObjects[metadataResponseProfile.name]
            self.assertIsInstance(metadataList, BorhanMetadataListResponse)
            self.assertEqual(len(metadataProfiles), len(metadataList.objects))
            [self.assertIsInstance(metadata, BorhanMetadata) for metadata in metadataList.objects]
            
            for metadata in metadataList.objects:
                self.assertEqual(entry.id, metadata.objectId)
                
                
def test_suite():
    return unittest.TestSuite((
        unittest.makeSuite(ResponseProfileTests)
        ))

if __name__ == "__main__":
    suite = test_suite()
    unittest.TextTestRunner(verbosity=2).run(suite)