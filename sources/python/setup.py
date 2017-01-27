from distutils.core import setup

setup(
    name='BorhanClient',
    version='1.0.0',
    url='http://www.borhan.com/api_v3/testme/client-libs.php',
    packages=['BorhanClient', 'BorhanClient.Plugins'],
    license='AGPL',
    description='A Python module for accessing the Borhan API.',
    long_description=open('README.txt').read(),
)
