#Below is the link to webpage
#url=https://web.archive.org/web/20230902185326/https://en.wikipedia.org/wiki/List_of_countries_by_GDP_%28nominal%29

from bs4 import BeautifulSoup
import requests
import pandas as pd
import numpy as np
import sqlite3
from datetime import datetime 
from bs4 import BeautifulSoup
url = 'https://web.archive.org/web/20230902185326/https://en.wikipedia.org/wiki/List_of_countries_by_GDP_%28nominal%29'
response=requests.get(url)
table_attr = ["Rank", "Bank_Name","Market_capitalization"]
db_name = 'Largest_banks.db'
table_name = 'Banks'
csv_path = '/home/project/Largest_banks_by_mc.csv'
html_page = requests.get(url).text
data = BeautifulSoup(html_page, 'html.parser')
tables = data.find_all('tbody')
rows = tables[0].find_all('tr')
df = pd.DataFrame(columns=table_attr)

if response.status_code == 200:
    # Open the specified file path in write-binary mode and save the content
    with open(csv_path, 'wb') as file:
        file.write(response.content)
    print(f'File successfully downloaded and saved to {csv_path}')
else:
    print(f'Failed to download the file. Status code: {response.status_code}')

for row in rows[1:]:
    col = row.find_all('td')
    if len(col) > 0:
        rank = col[0].text.strip()
        bank_name = col[1].text.strip()
        market_cap = col[2].text.strip()
        df = df.append({"Rank": rank, "Bank_Name": bank_name, "Market_capitalization": market_cap}, ignore_index=True)
    
print(df)

df.to_csv(csv_path)
conn = sqlite3.connect(db_name)
df.to_sql(table_name, conn, if_exists='replace', index=False)
conn.close()
